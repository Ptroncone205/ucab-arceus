package nintendont.amongspirits.utils;
import java.util.Random;

public class SpiritDataGenerator {
    private static final Random rand = new Random();
    private static final String[] names = {
        "Tian-Lang", "Yue-Tu", "Bai-Hu", "Ling-Lu", "Jiu-Wei",
        "Qing-Long", "Xuan-Wu", "Jin-Wu", "Mo-Lin", "Ying-Zhuo",
        "Fei-Lian", "Shen-Lu", "Xiao-Tian", "Kun-Peng", "Yu-Hu",
        "Cang-Lang", "Xue-Bao", "Feng-Huang", "Huo-Hu", "Mo-Qi",
        "Bai-Zhi", "Long-Wei", "Qiu-Li", "Bi-An", "Suan-Ni",
        "Tao-Tie", "Hun-Dun", "Qiong-Qi", "Ying-Long", "Jiao-Tu",
        "Chao-Feng", "Ya-Zi", "Chi-Wen", "Pu-Lao", "Bi-Xi",
        "Yuan-Fei", "He-Gu", "Zhu-Que", "Luo-Shen", "Teng-She",
        "Bai-Ze", "Qing-Luan", "Jin-Hou", "Mo-She", "Xian-He",
        "Yue-Lang", "Bing-Hu", "Shen-Gui", "Tian-Ma", "Lei-Shou"
    };
    private static final String[] lastNames = {
        "Lin", "Ye", "Gu", "Shen", "Bai",
        "Mo", "Feng", "Xiao", "Chu", "Han",
        "Zhao", "Qin", "Jiang", "Yuan", "Wei",
        "Lu", "Su", "Yan", "Xue", "Cang",
        "Zhong", "Qi", "Shao", "Yun", "Ming",
        "Huo", "Lan", "Sheng", "He", "Yue",
        "Long", "Qing", "Zuo", "Gong", "Fu",
        "Shi", "Rong", "Duan", "Zhu", "Ke",
        "Xie", "Mu", "Ji", "Ying", "You",
        "Tan", "Pei", "Luo", "Sui", "Jing"
    };
    private static final String[] bios = {
        "A tireless traveler who spent years mapping the highest mountain peaks. During a quiet sunset at the summit, they simply faded into the mist, leaving their physical form behind. Now, they drift between the crags as a silent guardian, leading lost hikers back to safety with a faint, glowing light.",
        "Known for a boundless curiosity, this soul spent their days chasing the wind across the open plains. One afternoon, while resting in a field of tall grass, they became as light as the air itself. They now roam the meadows, playing among the wildflowers and whispering to the breeze.",
        "A guardian of the deep woods who lived in a small hut made of fallen branches. After a long and peaceful life, they lay down under an ancient oak and woke up as a shimmering shadow. They continue to watch over the trees, ensuring the forest remains quiet and undisturbed.",
        "This person was a gifted musician whose melodies could calm the fiercest storms. At the end of a final, beautiful performance, their physical presence dissolved into the last echoing note. They now wander the riverbanks, their presence felt only as a soothing hum that brings peace to the weary.",
        "A humble gardener who could make anything bloom, even in the harshest winter. When their work was finally done, they drifted into a deep sleep and emerged as a spirit of growth. They travel the world now, leaving a trail of vibrant life and soft scents wherever they step.",
        "Obsessed with the stars, they spent every night staring at the cosmos from a high tower. One night, they reached out to touch a falling star and became a fragment of the starlight themselves. They now wander the night, a flicker of movement seen only out of the corner of the eye.",
        "A swift runner who loved the thrill of the chase and the feeling of the earth beneath them. When they finally grew tired, they merged with the golden horizon at dusk. They remain a blur of motion, racing across the world to see what lies beyond the next hill.",
        "A scholar who sought the wisdom of the old world through ancient scrolls. After closing their favorite book for the last time, they transitioned into a flickering spark of knowledge. They wander through ruins and libraries, protecting the stories and memories of those who came before.",
        "A brave soul who stood as a protector of their small village during many winters. When peace finally settled permanently over the land, they stepped into the shadows and became a watchful protector. They now roam the outskirts of civilization, keeping the dark and cold at bay.",
        "A playful spirit who loved to hide in the gardens of others, leaving small gifts behind. One day, they disappeared during a game of hide-and-seek and never truly returned to their old form. They now wander from house to house, bringing small moments of luck and joy to children.",
        "They spent their life tending to the great fires that kept their community warm. As the final embers of their last fire cooled, their spirit rose with the smoke. Now, they are a warm presence found near hearths and campfires, offering a sense of comfort to those traveling through the night.",
        "A weaver who created tapestries that told the history of the world. When the final thread was pulled, they became part of the fabric of reality. They wander the world now, mending the invisible fraying edges of nature and keeping the colors of the seasons bright and vivid.",
        "A quiet observer of the tides who lived by the sea for many decades. One morning, they walked into the surf and became as fluid as the waves themselves. They now wander the coastlines, a shimmering presence in the spray, guiding the currents and watching the ships pass by.",
        "Known for an incredible sense of direction, they were never lost, no matter how far they traveled. When their journey reached its natural end, they became a guiding light for others. They roam the world's most confusing paths, helping the uncertain find their way home without ever speaking a word.",
    };
    public String getName(){
        return names[rand.nextInt(names.length)];
    }
    public String getLastName(){
        return lastNames[rand.nextInt(lastNames.length)];
    }
    public String getBio(){
        return bios[rand.nextInt(bios.length)];
    }
}
