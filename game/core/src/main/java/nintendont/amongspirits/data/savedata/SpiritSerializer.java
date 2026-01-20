package nintendont.amongspirits.data.savedata;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.SpiritForm;
import nintendont.amongspirits.data.spirits.Spirit;

public class SpiritSerializer implements Json.Serializer<Spirit> {
    private Codex codex;

    public SpiritSerializer(Codex codex){
        this.codex = codex;
    }

    @Override
    public void write(Json json, Spirit object, Class knownType) {
        json.writeObjectStart();

        json.writeValue("id", object.getId());
        json.writeValue("name", object.getName());
        json.writeValue("lastname",object.getLastName());
        json.writeValue("bio", object.getBiography());
        json.writeValue("gender", object.getGender());
        json.writeValue("formID", object.getForm().getId());

        json.writeObjectEnd();
    }
    @Override
    public Spirit read(Json json, JsonValue jsonData, Class type) {
        int id = jsonData.getInt("id");
        int formID = jsonData.getInt("formID");
        String name = jsonData.getString("name");
        String lastname = jsonData.getString("lastname");
        String bio = jsonData.getString("bio");
        boolean genr = jsonData.getBoolean("gender");   

        SpiritForm form = codex.getFormById(formID); // el codex ya debe estar cargado

        return new Spirit(id, name, lastname, bio, genr, form);
    }
    
}
