const WebSocket = require("ws");

const wss = new WebSocket.Server({ port: 8080 });

console.log("Servidor WebSocket corriendo en ws://localhost:8080");

idCounter = 0;
players = [];

wss.on("connection", (ws) => {
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
      idCounter++;

      ws.send(
        JSON.stringify({
          type: "player_signedin",
          id: currentPlayer.id,
        })
      );

      broadcast(
        JSON.stringify({
          type: "player_connected",
          player: currentPlayer,
        }),
        ws
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
        ws
      );
    }
  });

  ws.on("close", () => {
    players = players.filter((p) => p.id !== currentPlayer.id);
    broadcast(
      JSON.stringify({
        type: "player_disconnected",
        id: currentPlayer.id,
      }),
      ws
    );
    console.log("Cliente desconectado");
  });

  ws.send(
    JSON.stringify({
      type: "all_players",
      players: players,
    })
  );
});

function broadcast(message, sender) {
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN && client !== sender) {
      client.send(message);
    }
  });
}
