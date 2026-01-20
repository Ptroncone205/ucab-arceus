const { time } = require("console");
const WebSocket = require("ws");

const wssWorld = new WebSocket.Server({ port: 8080 });

const playerSockets = new Map();
let players = [];
let items = JSON.parse(
  require("fs").readFileSync("./items_spawns.json", "utf8"),
);
let idCounter = 1;

wssWorld.on("connection", (ws) => {
  console.log("Nuevo cliente conectado");
  let currentPlayer = {};

  ws.on("message", (data) => {
    console.log(`Mensaje recibido: ${data}`);

    const packet = JSON.parse(data);

    if (packet.type == "player_signin") {
      currentPlayer = {
        id: idCounter,
        x: packet.x,
        y: packet.y,
        z: packet.z,
        rotX: packet.rotX,
        rotY: packet.rotY,
        rotZ: packet.rotZ,
      };

      players.push(currentPlayer);
      playerSockets.set(currentPlayer.id, ws);
      idCounter++;

      ws.send(
        JSON.stringify({
          type: "player_signedin",
          id: currentPlayer.id,
        }),
      );

      broadcast(
        JSON.stringify({
          type: "player_connected",
          player: currentPlayer,
        }),
        ws,
      );
    } else if (packet.type == "player_update") {
      currentPlayer.x = packet.x;
      currentPlayer.y = packet.y;
      currentPlayer.z = packet.z;
      currentPlayer.rotX = packet.rotX;
      currentPlayer.rotY = packet.rotY;
      currentPlayer.rotZ = packet.rotZ;

      broadcast(
        JSON.stringify({
          type: "player_moved",
          player: currentPlayer,
        }),
        ws,
      );
    } else if (packet.type == "player_challenge") {
      const targetSocket = playerSockets.get(packet.targetPlayerId);
      if (!targetSocket) {
        ws.send(
          JSON.stringify({
            type: "player_not_found",
            targetPlayerId: packet.targetPlayerId,
          }),
        );
        return;
      }

      targetSocket.send(
        JSON.stringify({
          type: "player_challenged",
          challengerID: currentPlayer.id,
          challenger: packet.challenger,
        }),
      );
    } else if (packet.type == "item_collected") {
      const targetItemId = packet.itemId;
      const targetSpawnIndex = packet.spawnIndex;
      const targetItem = items.find((item) => item.id === targetItemId);

      if (!targetItem) {
        ws.send(
          JSON.stringify({
            type: "item_not_found",
            itemId: targetItemId,
            spawnIndex: targetSpawnIndex,
          }),
        );
        return;
      }

      const targetSpawn = targetItem.spawns[targetSpawnIndex];

      if (!targetSpawn) {
        ws.send(
          JSON.stringify({
            type: "item_not_found",
            itemId: targetItemId,
            spawnIndex: targetSpawnIndex,
          }),
        );
        return;
      }

      const available = targetSpawn[0];
      if (!available) {
        ws.send(
          JSON.stringify({
            type: "item_unavailable",
            itemId: targetItemId,
            spawnIndex: targetSpawnIndex,
          }),
        );
        return;
      }

      targetSpawn[0] = false;
      broadcast(
        JSON.stringify({
          type: "item_collected_broadcast",
          itemId: targetItemId,
          spawnIndex: targetSpawnIndex,
        }),
        ws,
      );

      setTimeout(() => {
        targetSpawn[0] = true;
        broadcast(
          JSON.stringify({
            type: "item_respawned",
            itemId: targetItemId,
            spawnIndex: targetSpawnIndex,
          }),
          null,
        );
      }, 60000);
    }
  });

  ws.on("close", () => {
    playerSockets.delete(currentPlayer.id);
    players = players.filter((p) => p.id !== currentPlayer.id);
    broadcast(
      JSON.stringify({
        type: "player_disconnected",
        id: currentPlayer.id,
      }),
      ws,
    );
    console.log("Cliente desconectado");
  });

  ws.send(
    JSON.stringify({
      type: "world_setup",
      players: players,
      items: items,
    }),
  );
});

function broadcast(message, sender) {
  wssWorld.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN && client !== sender) {
      client.send(message);
    }
  });
}
