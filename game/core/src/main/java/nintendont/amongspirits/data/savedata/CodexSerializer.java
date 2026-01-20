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
        // 1. LOAD STATIC DATA
        // Instead of creating an empty Codex, we load the "Default" state
        // containing all names, descriptions, and moves.
        List<SpiritForm> forms = codex.getForms();

        // 2. APPLY SAVE DATA
        // jsonData represents the array we wrote earlier
        int formIndex = 0;
        
        // Iterate over the JSON objects (one per SpiritForm)
        for (JsonValue entry = jsonData.child; entry != null; entry = entry.next) {
            
            // Safety check: Don't crash if save file is longer than current game version
            if (formIndex >= forms.size()) break;

            SpiritForm form = forms.get(formIndex);
            
            // (Optional) Sanity Check: Verify IDs match
            // if (!entry.getString("id").equals(form.getId())) { Gdx.app.log("Codex", "ID Mismatch!"); }

            // Restore Level
            if (entry.has("lvl")) {
                // Assuming you have a setter for this
                form.setResearchLevel(entry.getInt("lvl")); 
            }

            // Restore Task Counts
            JsonValue tasksArray = entry.get("tasks");
            if (tasksArray != null) {
                List<ResearchTaskSet> taskSets = form.getTasks();
                int taskIndex = 0;

                // Iterate over the integers in the "tasks" array
                for (JsonValue countVal = tasksArray.child; countVal != null; countVal = countVal.next) {
                    if (taskIndex < taskSets.size()) {
                        // Restore the count
                        // Assuming ResearchTaskSet has a setter for current count
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