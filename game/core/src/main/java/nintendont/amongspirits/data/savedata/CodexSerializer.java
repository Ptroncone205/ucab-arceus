package nintendont.amongspirits.data.savedata;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import nintendont.amongspirits.data.codex.Codex;
import nintendont.amongspirits.data.codex.FakeCodexLoader;
import nintendont.amongspirits.data.codex.SpiritForm;
import nintendont.amongspirits.data.codex.ResearchTaskSet;

import java.util.List;

public class CodexSerializer implements Json.Serializer<Codex> {
    private Codex codex;
    public CodexSerializer (Codex codex){
        this.codex = codex;
    }
    @Override
    public void write(Json json, Codex codex, Class knownType) {
        json.writeArrayStart();

        for (SpiritForm form : codex.getForms()) {
            json.writeObjectStart();
            
            json.writeValue("id", form.getId());
            
            json.writeValue("lvl", form.getResearchLevel());

            json.writeArrayStart("tasks");
            for (ResearchTaskSet taskSet : form.getTasks()) {
                System.out.println("tasks: " + form.getTasks());
                json.writeValue(taskSet.getCurrentCount());
                System.out.println("task count: " + taskSet.getCurrentCount());
            }
            json.writeArrayEnd();

            json.writeObjectEnd();
        }

        json.writeArrayEnd();
    }

    @Override
    public Codex read(Json json, JsonValue jsonData, Class type) {
        List<SpiritForm> forms = codex.getForms();

        int formIndex = 0;
        
        for (JsonValue entry = jsonData.child; entry != null; entry = entry.next) {
            
            if (formIndex >= forms.size()) break;

            SpiritForm form = forms.get(formIndex);
            

            if (entry.has("lvl")) {
                form.setResearchLevel(entry.getInt("lvl")); 
            }

            JsonValue tasksArray = entry.get("tasks");
            if (tasksArray != null) {
                List<ResearchTaskSet> taskSets = form.getTasks();
                int taskIndex = 0;

                for (JsonValue countVal = tasksArray.child; countVal != null; countVal = countVal.next) {
                    if (taskIndex < taskSets.size()) {

                        taskSets.get(taskIndex).setCurrentCount(countVal.asInt());
                    }
                    taskIndex++;
                }
            }
            
            formIndex++;
        }

        return codex;
    }
}