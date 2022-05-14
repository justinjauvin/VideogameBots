package MagicTraining;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(info = "MagicTrainingBot", logo = "", version = 1.0, author = "jbird", name = "Magic Training")

public class MagicTrainingMain extends Script {
    private final int SEAMAN_ID = 3645;
    private final int SCORP_ID = 3024;
    private final int BANK_CLERK_ID = 1634;
    private final String [] lootList = {"Black bead", "Yellow bead", "White bead", "Red bead", "Mind talisman"};
    GroundItem loot;
    private Position [] toLesserDemon = {
            new Position(3229, 3220, 0), new Position(3233, 3227, 0),
            new Position(3228, 3234, 0),
            new Position(3221, 3240, 0), new Position(3208, 3238, 0),
            new Position(3200, 3236, 0), new Position(3196, 3228, 0),
            new Position(3185, 3224, 0), new Position(3175, 3222, 0),
            new Position(3163, 3218, 0), new Position(3151, 3216, 0),
            new Position(3139, 3215, 0), new Position(3128, 3220, 0),
            new Position(3111, 3196, 0), new Position(3113, 3180, 0),
            new Position(3110, 3167, 0),
    };
    private Position [] toDoor = {
            new Position(3107, 3163, 0)
    };

    private Position [] toExit = {
            new Position(3109, 3166, 0)
    };
    private Position [] wizardTowerKaramjaPath = {
            new Position(3114, 3183, 0), new Position(3112, 3190, 0),
            new Position(3113, 3197, 0),
            new Position(3113, 3204, 0), new Position(3107, 3213, 0),
            new Position(3106, 3214, 0),
            new Position(3102, 3229, 0), new Position(3101, 3239, 0),
            new Position(3101, 3243, 0),
            new Position(3104, 3255, 0), new Position(3102, 3271, 0),
            new Position(3093, 3282, 0), new Position(3083, 3279, 0),
            new Position(3071, 3276, 0), new Position(3065, 3263, 0),
            new Position(3058, 3253, 0), new Position(3048, 3247, 0),
            new Position(3039, 3237, 0), new Position(3031, 3236, 0),
            new Position(3025, 3224, 0), new Position(3027, 3218, 0),
    };

    private Position[] pathToImpSpot = {
            new Position(2939, 3146, 0), new Position(2922, 3150, 0),
            new Position(2908, 3153, 0), new Position(2897, 3147, 0),
            new Position(2879, 3149, 0), new Position(2870, 3150, 0),
            new Position(2863, 3148, 0),
            new Position(2848, 3149, 0), new Position(2840, 3153, 0),
            new Position(2831, 3161, 0), new Position(2833, 3171, 0),
            new Position(2832, 3179, 0)
    };

    private Position[] pathToVarrock = {
            new Position(3196, 3430, 0), new Position(3193, 3448, 0),
            new Position(3177, 3458, 0), new Position(3167, 3472, 0),
            new Position(3164, 3483, 0), new Position(3164, 3488, 0)
    };

    private Position [] clickInDemonRoom= {
            new Position(3112, 3160, 0)
    };

    private Position [] backToImpSpot = {
            new Position(2832, 3179, 0)
    };

    @Override
    public int onLoop() throws InterruptedException {

        setWindStrike();

        lumbridgeToDemon();

        upWizardTower();

        while(skills.getStatic(Skill.MAGIC) < 5){
          sleep(Math.abs(randomNumber()+2000));
          if (isReadyToAttackDemon()) {
              if (!getInventory().isFull())
                  attackingDemon();
          }
        }

        setWaterStrike();

        while(skills.getStatic(Skill.MAGIC) < 16){
            sleep(Math.abs(randomNumber()+2000));
            if (isReadyToAttackDemon()) {
                if (!getInventory().isFull())
                    attackingDemon();
            }
        }
        setFireStrike();

        downWizardTower();

        wizardTowerToKaramja();

        karamjaPortToImpSpot();

        while(!getInventory().isFull() && skills.getDynamic(Skill.MAGIC) < 25){
            if(lootExists() && !getInventory().isFull()){
                loot();
                sleep(Math.abs(randomNumber())+2000);
            }
            else if (isReadyToAttackImp()) {
                attackingImp();
            }
            else if(myPlayer().isUnderAttack()){
                NPC scorp = getNpcs().closest(SCORP_ID);
                if(scorp.interact("Attack")){
                    for (int i = 0; i < backToImpSpot.length; i++) {
                        getWalking().walk(backToImpSpot[i]);
                        sleep(Math.abs(randomNumber())+2000);
                    }
                }
            }
            else if(getNpcs().closest("Imp") == null && !myPlayer().isMoving()){
                for (int i = 0; i < backToImpSpot.length; i++) {
                    getWalking().walk(backToImpSpot[i]);
                    sleep(Math.abs(randomNumber())+1500);
                }
            }
        }

        if(skills.getStatic(Skill.MAGIC) >= 25) {
            sleep(Math.abs(randomNumber())+2000);
            teleOut();

            sleep(Math.abs(randomNumber())+2000);
            varrockToGE();

        }

        sleep(Math.abs(randomNumber())+1000);
        stop();

        return 0;
    }

    public int randomNumber(){
        int variance = (int)(Math.random()*5938);
        int num = (int)(Math.random()*variance) + 1;
        if(num % 2 == 0)
            num*=-1;

        log("Sleep Variance: "+num);
        log("Variance: "+variance);
        log(" Attack Sleep Time: "+(7527+num));
        return num;
    }

    private boolean lootExists() throws InterruptedException {
        for (int i = 0; i < lootList.length; i++) {
            loot = groundItems.closest(lootList[i]);
            if (loot != null && loot.exists() && loot.isVisible()) {
                sleep(Math.abs(randomNumber())+2000);
                log("Loot exists!");
                return true;
            }
        }
        return false;
    }

    private void loot() throws InterruptedException {
        for (int i = 0; i < lootList. length; i++) {
            loot = groundItems.closest(lootList[i]);
            if (loot != null && loot.exists() && getMap().canReach(loot) && loot.isVisible()) {
                log("loot: " + loot);
                loot.interact("Take");
                sleep(Math.abs(randomNumber())+3500);
                if(loot.interact("Take"))
                    sleep(Math.abs(randomNumber())+3500);
                log("Taking loot");

            }
        }
    }

    public void setWindStrike() throws InterruptedException {
        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+1000);

        if(getInventory().contains("Staff of fire")){
            getInventory().interact("Wield", 1387);
        }
        sleep(Math.abs(randomNumber())+1000);

        getTabs().open(Tab.ATTACK);
        sleep(Math.abs(randomNumber())+2000);

        RS2Widget styleMagic = getWidgets().get(593, 26, 4);
        styleMagic.interact();
        sleep(Math.abs(randomNumber())+2000);


        RS2Widget chooseWindStrike = getWidgets().get(201, 1, 1);
        chooseWindStrike.interact("Wind Strike");
        sleep(Math.abs(randomNumber())+2000);

        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+2000);
    }

    public void setWaterStrike() throws InterruptedException {
        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+2000);

        if(getInventory().contains("Staff of fire")){
            getInventory().interact("Wield", 1387);
        }
        sleep(Math.abs(randomNumber())+2000);

        getTabs().open(Tab.ATTACK);
        sleep(Math.abs(randomNumber())+1000);


        RS2Widget styleMagic = null;

        while(styleMagic == null) {
            styleMagic = getWidgets().get(593, 26, 4);
            styleMagic.interact();
            sleep(Math.abs(randomNumber())+2000);
        }

        RS2Widget chooseWaterStrike = null;

        while(chooseWaterStrike == null) {
            chooseWaterStrike = getWidgets().get(201, 1, 2);
            chooseWaterStrike.interact("Water Strike");
            sleep(Math.abs(randomNumber())+2000);
        }

        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+2000);
    }

    public void setFireStrike() throws InterruptedException {
        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+2000);

        if(getInventory().contains("Staff of fire")){
            getInventory().interact("Wield", 1387);
        }
        sleep(Math.abs(randomNumber())+2000);

        getTabs().open(Tab.ATTACK);
        sleep(Math.abs(randomNumber())+1000);


        RS2Widget styleMagic = getWidgets().get(593, 26, 4);
        styleMagic.interact();
        sleep(Math.abs(randomNumber())+3000);


        RS2Widget chooseFireStrike = getWidgets().get(201, 1, 4);
        chooseFireStrike.interact();
        sleep(Math.abs(randomNumber())+3000);

        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+2000);
    }

    public void lumbridgeToDemon() throws InterruptedException {
        for(int i = 0; i < toLesserDemon.length; i++) {
            getWalking().walk(toLesserDemon[i]);
            sleep(Math.abs(randomNumber())+1500);
        }
    }

    public boolean isReadyToAttackDemon() throws InterruptedException {
        NPC enemy = getNpcs().closest("Lesser demon");

        if(getInventory().isFull()) {
            log("Is not3 Ready to attack");
            return false;
        }
        if((enemy == null)){
            hopWorldsDemon();
            log("Is not4 Ready to attack");
            return false;
        }
        if((enemy.getHealthPercent() <=0)){
            hopWorldsDemon();
            log("Is not5 Ready to attack");
            return false;
        }
        log("Is Ready to attack");
        return true;
    }

    public void attackingDemon() throws InterruptedException {
        NPC enemy = getNpcs().closest("Lesser demon");

        if((enemy == null)){
            hopWorldsDemon();
            return;
        }
        if((enemy.getHealthPercent() <=0)){
            return;
        }
        if(enemy.getHealthPercent() > 0) {
            if (enemy.interact("Attack")) {
                sleep(Math.abs(randomNumber())+1000);
                mouse.moveOutsideScreen();
                sleep(8527 + randomNumber());
            }
        }
    }

    public boolean isReadyToAttackImp() throws InterruptedException {
        NPC enemy = getNpcs().closest("Imp");

        if(getCombat().isFighting() || combat.isFighting()) {
            log("Is not1 Ready to attack");
            return false;
        }
        if(myPlayer().isAnimating() || myPlayer().isUnderAttack() || myPlayer().isMoving()) {
            log("Is not2 Ready to attack");
            return false;
        }
        if(getInventory().isFull()) {
            log("Is not3 Ready to attack");
            return false;
        }
        if(getInventory().isFull()) {
            log("Is not3 Ready to attack");
            return false;
        }
        if(!hasRunes()) {
            teleOut();
            log("Is not3 Ready to attack");
            return false;
        }
        if((enemy == null)){
            for (int i = 0; i < backToImpSpot.length; i++) {
                getWalking().walk(backToImpSpot[i]);
                sleep(Math.abs(randomNumber())+1000);
            }
            log("Is not4 Ready to attack");
            return false;
        }
        if(enemy.isUnderAttack()){
            for (int i = 0; i < backToImpSpot.length; i++) {
                getWalking().walk(backToImpSpot[i]);
                sleep(Math.abs(randomNumber())+1500);
            }
            log("Is not5 Ready to attack");
            return false;
        }
        log("Is Ready to attack");
        return true;
    }
    public void attackingImp() throws InterruptedException {
        NPC enemy = getNpcs().closest("Imp");
        if(getCombat().isFighting() || combat.isFighting())
            return;
        if(myPlayer().isAnimating() || myPlayer().isUnderAttack() || myPlayer().isMoving())
            return;
        if(getInventory().isFull())
            return;
        if((enemy == null)){
            for (int i = 0; i < backToImpSpot.length; i++) {
                getWalking().walk(backToImpSpot[i]);
                sleep(Math.abs(randomNumber())+1500);
            }
            return;
        }
        if(enemy.isUnderAttack() || (enemy.getHealthPercent() <=0)){
            return;
        }
        if(enemy.getHealthPercent() > 0) {
            if (enemy.interact("Attack")) {
                sleep(Math.abs(randomNumber())+1000);
                mouse.moveOutsideScreen();
                sleep(11527 + randomNumber());
            }
        }
    }

    public void changeLeftClickAttackOption() throws InterruptedException {
        sleep(Math.abs(randomNumber())+2000);
        getTabs().open(Tab.SETTINGS);
        sleep(Math.abs(randomNumber())+2000);

        RS2Widget settingsButton = getWidgets().get(116, 104);
        settingsButton.interact();
        sleep(Math.abs(randomNumber())+2000);

        RS2Widget downArrow = getWidgets().get(116, 7, 3);
        downArrow.interact();
        sleep(Math.abs(randomNumber())+2000);

        RS2Widget leftClickOption = getWidgets().get(116, 80, 3);
        leftClickOption.interact();
        sleep(Math.abs(randomNumber())+2000);

        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+2000);
    }

    public void upWizardTower() throws InterruptedException {
        RS2Object door = getObjects().closest("Door");
        if(door.hasAction("Open")) {
            door.interact("Open");
            sleep(Math.abs(randomNumber())+2000);
        }

        for(int i = 0; i < toDoor.length; i++) {
            getWalking().walk(toDoor[i]);
            sleep(Math.abs(randomNumber())+3000);
        }

        door = getObjects().closest("Door");
        if(door.hasAction("Open")) {
            door.interact("Open");
            sleep(Math.abs(randomNumber())+3000);
        }

        objects.getObjects().closest(12536).interact("Climb-up");
        sleep(Math.abs(randomNumber())+4000);

        objects.closest(12537).interact("Climb-up");
        sleep(Math.abs(randomNumber())+4000);

        door = getObjects().closest("Door");
        if(door.hasAction("Open")) {
            door.interact("Open");
            sleep(Math.abs(randomNumber())+3000);
        }

        for(int i = 0; i < clickInDemonRoom.length; i++) {
            getWalking().walk(clickInDemonRoom[i]);
            sleep(Math.abs(randomNumber())+3000);
        }
    }

    public void downWizardTower() throws InterruptedException {

        // **GOOD DOOR/GATE HANDLER!!!!!!!!!!!!!!!!!!!!!!!!!!**

        RS2Object door = getObjects().closest("Door");
        if(door.hasAction("Open")) {
            door.interact("Open");
            sleep(Math.abs(randomNumber())+3000);
        }


        objects.closest(12538).interact("Climb-down");
        sleep(Math.abs(randomNumber())+5000);

        objects.closest(12537).interact("Climb-down");
        sleep(Math.abs(randomNumber())+5000);

        door = getObjects().closest("Door");
        if(door.hasAction("Open")) {
            door.interact("Open");
            sleep(Math.abs(randomNumber())+4000);
        }

        for(int i = 0; i < toExit.length; i++) {
            getWalking().walk(toExit[i]);
            sleep(Math.abs(randomNumber())+3000);
        }

        door = getObjects().closest("Door");
        if(door.hasAction("Open")) {
            door.interact("Open");
            sleep(Math.abs(randomNumber())+3000);
        }

    }



    public void hopWorldsDemon() throws InterruptedException {
        //check where scroll bar widget clicks
        int num;
        do {
            num = (int) (Math.random() * 13) + 536;
        }while(getWorlds().getCurrentWorld() == num);

        getTabs().open(Tab.LOGOUT);
        sleep(Math.abs(randomNumber())+1000);

        RS2Widget worldSwitcher = getWidgets().get(182, 3);
        if(worldSwitcher != null) {
            worldSwitcher.interact();
            sleep(Math.abs(randomNumber())+1000);
        }

            RS2Widget downArrow = getWidgets().get(69, 18, 5);
            downArrow.interact();
        sleep(Math.abs(randomNumber())+1000);


        getWorlds().hop(num);
        sleep(Math.abs(randomNumber())+1000);

        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+1000);
        return;
    }

    public void wizardTowerToKaramja() throws InterruptedException {
        for(int i = 0; i < wizardTowerKaramjaPath.length; i++) {
            getWalking().walk(wizardTowerKaramjaPath[i]);
            sleep(Math.abs(randomNumber())+2000);
        }
        sleep(Math.abs(randomNumber()) + 6000);
        NPC seaman = getNpcs().closest(SEAMAN_ID);
        seaman.interact("Pay-fare");
        sleep(Math.abs(randomNumber()) + 7000);

        for(int i = 0; i < 3; i++) {
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber()) + 3000);
        }
        for(int i = 0; i < 3; i++) {
            dialogues.getDialogues().selectOption(1);
            sleep(Math.abs(randomNumber()) + 3000);
        }
        for(int i = 0; i < 2; i++) {
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber()) + 15000);
        }
        objects.closest("Gangplank").interact("Cross");
        sleep(Math.abs(randomNumber()) + 4000);
    }

    public void karamjaPortToImpSpot() throws InterruptedException {
        for (int i = 0; i < pathToImpSpot.length; i++) {
            getWalking().walk(pathToImpSpot[i]);
            sleep(Math.abs(randomNumber()+1500));
        }
    }

    public void teleOut() throws InterruptedException {
        getTabs().open(Tab.MAGIC);
        sleep(Math.abs(randomNumber())+1000);
        getMagic().castSpell(Spells.NormalSpells.VARROCK_TELEPORT);
        sleep(Math.abs(randomNumber())+1000);
        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+2000);
    }

    public void varrockToGE() throws InterruptedException {
        for (int i = 0; i < pathToVarrock.length; i++) {
            getWalking().walk(pathToVarrock[i]);
            sleep(Math.abs(randomNumber())+2000);
        }
    }

    public boolean hasRunes(){
        if(getInventory().contains("Air rune") && getInventory().getAmount("Air rune") >=5 &&
                getInventory().contains("Mind rune") && getInventory().getAmount("Mind rune") > 0){
            log("Has Runes");
            return true;
        }
        else{
            log("No Runes");
            return false;
        }
    }

    public void firstTimeBankTutorial() throws InterruptedException {
        sleep(Math.abs(randomNumber())+2000);
        NPC bankNPC = getNpcs().closest(BANK_CLERK_ID);
        bankNPC.interact("Bank");
    }
}
