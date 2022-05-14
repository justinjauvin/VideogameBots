package tutorialislandPackage;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import java.awt.*;

@ScriptManifest(info = "Tutorial Island Bot", logo = "", version = 1.0, author = "jbird", name = "Tutorial Island")

public class tutorialislandMain extends Script {
    private final int FIRSTGUY_ID = 3308;
    private final int FISHGUY_ID = 8503;
    private final int FISHSPOT_ID = 3317;
    private final int CHEF_ID = 3305;
    private final int QUESTGUY_ID = 3312;
    private final int MININGGUY_ID = 3311;
    private final int COMBATGUY_ID = 3307;
    private final int ACCOUNTGUY_ID = 3310;
    private final int PRAYERGUY_ID = 3319;
    private final int MAGICGUY_ID = 3309;
    private final int GE_CLERK_ID = 2149;
    private final int BANK_CLERK_ID = 1634;
    private final int [] SUBROOT_IDS = {13, 17, 21, 25, 29, 33, 37, 44, 48, 52, 56, 60};

    private Position [] toFishingSpot = {
            new Position(3103, 3100, 0), new Position(3098, 3100, 0),
    };
    private Position [] toGate = {
            new Position(3097, 3094, 0), new Position(3092, 3092, 0),
    };
    private Position [] toBreadMan = {
            new Position(3087, 3086, 0), new Position(3082, 3083, 0),
    };
    private Position [] toBreadmanExit = {
            new Position(3074, 3090, 0)
    };
    private Position [] toQuestArea = {
            new Position(3071, 3099, 0), new Position(3069, 3108, 0),
            new Position(3070, 3116, 0), new Position(3075, 3124, 0),
            new Position(3085, 3127, 0)

    };
    private Position [] toMiningSpot = {
            new Position(3081, 9519, 0), new Position(3082, 9507, 0),
    };
    private Position [] toMeleeSpot = {
            new Position(3085, 9506, 0), new Position(3093, 9502, 0)
    };
    private Position [] toMeleeGuy = {
            new Position(3100, 9504, 0)
    };
    private Position [] toGateCombat = {
            new Position(3112, 9518, 0)
    };
    private Position [] leavingCombatArea = {
            new Position(3113, 9518, 0), new Position(3110, 9524, 0)
    };
    private Position [] toPrayerArea = {
            new Position(3136, 3115, 0), new Position(3133, 9108, 0),
            new Position(3124, 3107, 0)
    };
    private Position [] toMagicArea = {
            new Position(3127, 3096, 0), new Position(3135, 3088, 0),
            new Position(3141, 3089, 0)
    };
    private Position [] toChickens = {
            new Position(3236, 3225, 0), new Position(3245, 3226, 0),
            new Position(3258, 3229, 0), new Position(3260, 3237, 0),
            new Position(3256, 3246, 0), new Position(3250, 3254, 0),
            new Position(3251, 3263, 0),
            new Position(3246, 3272, 0), new Position(3240, 3280, 0),
            new Position(3238, 3288, 0), new Position(3238, 3295, 0)
    };
    private Position [] chickensToVarrock = {
            new Position(3239, 3302, 0), new Position(3242, 3309, 0),
            new Position(3249, 3319, 0), new Position(3257, 3322, 0),
            new Position(3266, 3326, 0), new Position(3259, 3332, 0),
            new Position(3249, 3336, 0), new Position(3237, 3337, 0),
            new Position(3227, 3341, 0), new Position(3224, 3349, 0),
            new Position(3216, 3358, 0), new Position(3211, 3363, 0),
            new Position(3211, 3370, 0),
            new Position(3209, 3377, 0), new Position(3210, 3387, 0),
            new Position(3211, 3396, 0), new Position(3210, 3403, 0),
            new Position(3202, 3412, 0), new Position(3198, 3419, 0),
            new Position(3192, 3428, 0), new Position(3178, 3434, 0),
            new Position(3176, 3446, 0), new Position(3169, 3453, 0),
            new Position(3164, 3462, 0), new Position(3164, 3470, 0),
            new Position(3162, 3481, 0), new Position(3164, 3486, 0),
    };

    private Position [] pathGEtoVarrock = {
            new Position(3166, 3480, 0), new Position(3164, 3472, 0),
            new Position(3171, 3460, 0), new Position(3182, 3453, 0),
            new Position(3191, 3448, 0), new Position(3197, 3439, 0),
            new Position(3201, 3438, 0),
            new Position(3207, 3437, 0), new Position(3217, 3432, 0),
            new Position(3215, 3424, 0), new Position(3217, 3417, 0),
    };
    private Position [] firstClick = {
            new Position(3232, 3229, 0),
    };
    private Position [] firstClickOutOfBank = {
            new Position(3168, 3486, 0),
    };

    private Position [] toTree = {
            new Position(3105, 3100, 0)
    };

    private final String [] lootList = {"Bones", "Raw chicken"};
    GroundItem loot;
    @Override
    public int onLoop() throws InterruptedException {

        beforeSoundOff();

        setToFixedLayout();

        turnSoundOff();

        afterSoundOff();

        fishGuySection();

        breadManSection();

        questSection();

        miningSection();

        combatSection();

        bankSection();

        prayerSection();

        magicSection();

        firstClickOffTutorialIsland();

        hideRoofs();

        changeLeftClickAttackOption();

        toChickenPen();

        do {
            if (lootExists()) {
                loot();
            } else if (isReadyToAttack()) {
                if (!getInventory().isFull())
                    attacking();
            }
        }while(getInventory().getAmount("Bones") <= 5 && getInventory().getAmount("Raw chicken") <= 5);

        chickensToGE();

        sellAllChicken();

        firstTimeBankTutorial();

        GEtoVarrock();

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
                sleep(Math.abs(randomNumber())+2500);
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

    public void keyboardInput() throws InterruptedException, AWTException {
        sleep(Math.abs(randomNumber())+2000);
        getKeyboard().typeString("falp");
    }

    public void afterSoundOff() throws InterruptedException {
        NPC firstGuy = getNpcs().closest(FIRSTGUY_ID);
        firstGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+5000);

        for(int i = 0; i<2; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+1000);
        }

        objects.closest(9398).interact("Open");
        sleep(Math.abs(randomNumber())+2000);

        for (int i = 0; i < toFishingSpot.length; i++) {
            getWalking().walk(toFishingSpot[i]);
            sleep(Math.abs(randomNumber())+1500);
        }
    }

    public void fishGuySection() throws InterruptedException {

        NPC fishGuy = getNpcs().closest(FISHGUY_ID);
        do {
            fishGuy.interact("Talk-to");
            sleep(Math.abs(randomNumber())+1000);

            for(int i = 0; i<5; i++){
                dialogues.clickContinue();
                sleep(Math.abs(randomNumber())+1000);
            }

            sleep(Math.abs(randomNumber())+3000);
            for(int i = 0; i<8; i++) {
                RS2Widget inventoryTab = getWidgets().get(548, 59);
                inventoryTab.interact();
                sleep(Math.abs(randomNumber())+2000);
            }

                NPC fishSpot = getNpcs().closest(FISHSPOT_ID);
                fishSpot.interact("Net");
            sleep(Math.abs(randomNumber())+10000);;
        }while(!inventory.getInventory().contains(2514));

        sleep(Math.abs(randomNumber())+3000);
        for(int i = 0; i<8; i++) {
            RS2Widget skillTab = getWidgets().get(548, 57);
            skillTab.interact();
            sleep(Math.abs(randomNumber())+2000);
        }
        fishGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+1000);

        for(int i = 0; i<3; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+1000);
        }

        sleep(Math.abs(randomNumber())+3000);
        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+1000);

        do {
            for (int i = 0; i < toTree.length; i++) {
                getWalking().walk(toTree[i]);
                sleep(Math.abs(randomNumber())+1500);
            }

            do {
                objects.closest(9730).interact("Chop down");
                sleep(Math.abs(randomNumber())+5000);
            } while (!inventory.contains("Logs"));


            inventory.getItem("Logs").interact("Use");
            sleep(Math.abs(randomNumber())+5000);

            inventory.getItem("Tinderbox").interact();
            sleep(Math.abs(randomNumber())+8000);

            inventory.getItem("Raw shrimps").interact("Use");
            sleep(Math.abs(randomNumber())+4000);

            objects.closest(26185).interact();
            sleep(Math.abs(randomNumber())+5000);
        }while(!getPlayers().getInventory().contains("Shrimps"));
    }

    public void breadManSection() throws InterruptedException {
        for (int i = 0; i < toGate.length; i++) {
            getWalking().walk(toGate[i]);
            sleep(Math.abs(randomNumber())+1500);
        }

        objects.closest(9470).interact("Open");
        sleep(Math.abs(randomNumber())+4000);

        for (int i = 0; i < toBreadMan.length; i++) {
            getWalking().walk(toBreadMan[i]);
            sleep(Math.abs(randomNumber())+1500);
        }

        objects.closest(9709).interact("Open");
        sleep(Math.abs(randomNumber())+8000);

        NPC chef = getNpcs().closest(CHEF_ID);
        chef.interact("Talk-to");
        sleep(Math.abs(randomNumber())+4000);

        for(int i = 0; i<4; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+1000);
        }

        inventory.getItem("Bucket of water").interact("Use");
        sleep(Math.abs(randomNumber())+2000);

        inventory.getItem("Pot of flour").interact();
        sleep(Math.abs(randomNumber())+2000);

        objects.closest(9736).interact("Cook");
        sleep(Math.abs(randomNumber())+8000);

        for (int i = 0; i < toBreadmanExit.length; i++) {
            getWalking().walk(toBreadmanExit[i]);
            sleep(Math.abs(randomNumber())+1500);
        }

        objects.closest(9710).interact("Open");
        sleep(Math.abs(randomNumber())+3400);
    }

    public void questSection() throws InterruptedException {
        for (int i = 0; i < toQuestArea.length; i++) {
            getWalking().walk(toQuestArea[i]);
            sleep(Math.abs(randomNumber())+1500);
        }

        objects.closest(9716).interact("Open");
        sleep(Math.abs(randomNumber())+6000);

        NPC questGuy = getNpcs().closest(QUESTGUY_ID);
        questGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+4000);

        for(int i = 0; i<2; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }

        for(int i = 0; i<7; i++) {
            RS2Widget clickPrayerTab = getWidgets().get(548, 58);
            clickPrayerTab.interact();
            sleep(Math.abs(randomNumber())+4000);

        }

        questGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+3000);

        for(int i = 0; i<9; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }

        objects.closest(9726).interact("Climb-down");
        sleep(Math.abs(randomNumber())+3000);
    }

    public void miningSection() throws InterruptedException {
        sleep(Math.abs(randomNumber())+6000);
        for (int i = 0; i < toMiningSpot.length; i++) {
            getWalking().walk(toMiningSpot[i]);
            sleep(Math.abs(randomNumber())+1500);
        }

        NPC miningGuy = getNpcs().closest(MININGGUY_ID);
        miningGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+4000);

        for(int i = 0; i<6; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }
        do {
            do {
                objects.closest(10080).interact("Mine");
                sleep(Math.abs(randomNumber())+7000);
            } while (!inventory.contains("Tin ore"));

            do {
                objects.closest(10079).interact("Mine");
                sleep(Math.abs(randomNumber())+7000);
            } while (!inventory.contains("Copper ore"));

            objects.closest(10082).interact("Use");
            sleep(Math.abs(randomNumber())+8000);
        }while(!inventory.contains("Bronze bar"));

        sleep(Math.abs(randomNumber())+2000);
        miningGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+2000);

        for (int i = 0; i < 3; i++) {
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }

        do{
            objects.closest(2097).interact("Smith");
            sleep(Math.abs(randomNumber())+6000);


            RS2Widget smithBronzeDagger = getWidgets().get(312, 9, 2);
            smithBronzeDagger.interact("Smith");
            sleep(Math.abs(randomNumber())+7000);
        }while(!inventory.contains("Bronze dagger"));

        for (int i = 0; i < toMeleeSpot.length; i++) {
            getWalking().walk(toMeleeSpot[i]);
            sleep(Math.abs(randomNumber())+1500);
        }

        objects.closest(9718).interact("Open");
        sleep(Math.abs(randomNumber())+3000);

    }

    public void combatSection() throws InterruptedException {
        for (int i = 0; i < toMeleeGuy.length; i++) {
            getWalking().walk(toMeleeGuy[i]);
            sleep(Math.abs(randomNumber())+1500);
        }

        sleep(Math.abs(randomNumber())+3000);
        NPC combatGuy = getNpcs().closest(COMBATGUY_ID);
        combatGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+3000);

        for(int i = 0; i<3; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+3000);
        }

        //potential good flickering tab open widget***!!!!***!!!***!!!!
        RS2Widget equipmentTab = null;
        while (equipmentTab == null){
            for(int i = 0; i < 6; i++) {
                equipmentTab = getWidgets().get(548, 60);
                equipmentTab.interact();
                sleep(Math.abs(randomNumber())+3000);
            }
        }
        RS2Widget equipmentStats = getWidgets().get(387, 2);
        equipmentStats.interact();
        sleep(Math.abs(randomNumber())+3000);

        combatGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+2000);

        for(int i = 0; i<3; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }
        while(getPlayers().getInventory().contains(1205)) {
            inventory.getInventory().interact("Wield", 1205);
            sleep(Math.abs(randomNumber()) + 2000);
        }
        combatGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+2000);

        for(int i = 0; i<2; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }

        while(getPlayers().getInventory().contains(1277)) {
            inventory.getInventory().interact("Wield", 1277);
            sleep(Math.abs(randomNumber()) + 2000);
        }
        while(getPlayers().getInventory().contains(1171)) {
            inventory.getInventory().interact("Wield", 1171);
            sleep(Math.abs(randomNumber()) + 2000);
        }
        for(int i = 0; i<8; i++) {
            RS2Widget combatStyleTab = getWidgets().get(548, 56);
            combatStyleTab.interact();
            sleep(Math.abs(randomNumber())+4000);
        }

        for (int i = 0; i < toGateCombat.length; i++) {
            getWalking().walk(toGateCombat[i]);
            sleep(Math.abs(randomNumber())+1500);
        }

        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+3000);

        objects.closest(9719).interact("Open");
        sleep(Math.abs(randomNumber())+3000);

        NPC rat = getNpcs().closest(3313);
        rat.interact("Attack");
        sleep(Math.abs(randomNumber())+35000);

        objects.closest(9719).interact("Open");
        sleep(Math.abs(randomNumber())+3000);

        combatGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+8000);

        for(int i = 0; i<4; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }
        while(getPlayers().getInventory().contains(841)) {
            inventory.getInventory().interact("Wield", 841);
            sleep(Math.abs(randomNumber()) + 3000);
        }
        while(getPlayers().getInventory().contains(882)) {
            inventory.getInventory().interact("Wield", 882);
            sleep(Math.abs(randomNumber()) + 3000);
        }
        rat.interact("Attack");
        sleep(Math.abs(randomNumber())+35000);

        for (int i = 0; i < leavingCombatArea.length; i++) {
            getWalking().walk(leavingCombatArea[i]);
            sleep(Math.abs(randomNumber())+1500);
        }

        objects.closest(9727).interact("Climb-up");
        sleep(Math.abs(randomNumber())+3000);
    }

    public void bankSection() throws InterruptedException {
        objects.closest(10083).interact("Use");
        sleep(Math.abs(randomNumber())+13000);

        RS2Widget xButton = getWidgets().get(12, 2, 11);
        xButton.interact();
        sleep(Math.abs(randomNumber())+2000);

        objects.closest(26815).interact("Use");
        sleep(Math.abs(randomNumber())+3000);

        for(int i = 0; i<5; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+4000);
        }


        RS2Widget xButton2 = getWidgets().get(345, 2, 11);
        if(xButton2 != null) {
            xButton2.interact();
        }
        sleep(Math.abs(randomNumber())+2000);

        objects.closest(9721).interact("Open");
        sleep(Math.abs(randomNumber())+7000);

        NPC accountGuy = getNpcs().closest(ACCOUNTGUY_ID);
        accountGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+3000);

        for(int i = 0; i<6; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }

        for(int i = 0; i<8; i++) {
            RS2Widget accountManagementTab = getWidgets().get(548, 40);
            accountManagementTab.interact();
            sleep(Math.abs(randomNumber())+2000);
        }
        accountGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+4000);

        for(int i = 0; i<18; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+1000);
        }

        objects.closest(9722).interact("Open");
        sleep(Math.abs(randomNumber())+5000);
    }

    public void prayerSection() throws InterruptedException {
        for (int i = 0; i < toPrayerArea.length; i++) {
            getWalking().walk(toPrayerArea[i]);
            sleep(Math.abs(randomNumber())+1500);
        }

        NPC prayerGuy = getNpcs().closest(PRAYERGUY_ID);
        prayerGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+3000);

        for(int i = 0; i<3; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+1500);
        }

        for(int i = 0; i<8; i++) {
            RS2Widget clickPrayerTab = getWidgets().get(548, 61);
            clickPrayerTab.interact();
            sleep(Math.abs(randomNumber())+3000);
        }

        prayerGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+2000);

        for(int i = 0; i<5; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }

        for(int i = 0; i<8; i++) {
            RS2Widget clickFriendsTab = getWidgets().get(548, 48);
            clickFriendsTab.interact();
            sleep(Math.abs(randomNumber())+1500);
        }
        prayerGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+2000);

        for(int i = 0; i<4; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }

        objects.closest(9723).interact("Open");
        sleep(Math.abs(randomNumber())+2000);

        for (int i = 0; i < toMagicArea.length; i++) {
            getWalking().walk(toMagicArea[i]);
            sleep(Math.abs(randomNumber())+1500);
        }
    }

    public void magicSection() throws InterruptedException {

        NPC magicGuy = getNpcs().closest(MAGICGUY_ID);
        magicGuy.interact("Talk-to");

        sleep(Math.abs(randomNumber())+8000);


        for(int i = 0; i<3; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }
        for(int i = 0; i<8; i++) {
            RS2Widget clickMagicTab = getWidgets().get(548, 69);
            clickMagicTab.interact();
            sleep(Math.abs(randomNumber())+2000);
        }
        magicGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+3000);

        for(int i = 0; i<3; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }

        //could try while exp of magic skill == 0 keep trying to cast on chicken*******!!!!!!******
        RS2Widget selectWindStrike = getWidgets().get(218, 6);
        selectWindStrike.interact();
        sleep(Math.abs(randomNumber())+3000);

        NPC chicken = getNpcs().closest(3316);
        chicken.interact();
        sleep(Math.abs(randomNumber())+5000);

        magicGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+2000);

        for(int i = 0; i<2; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }

        dialogues.getDialogues().selectOption(1);
        sleep(Math.abs(randomNumber())+2000);

        dialogues.getDialogues().selectOption(1);
        sleep(Math.abs(randomNumber())+2000);

        for(int i = 0; i<2; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }

        dialogues.getDialogues().selectOption(3);
        sleep(Math.abs(randomNumber())+2000);

        dialogues.getDialogues().selectOption(3);
        sleep(Math.abs(randomNumber())+2000);

        for(int i = 0; i<7; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }
    }

    public void toChickenPen() throws InterruptedException {

        sleep(Math.abs(randomNumber())+2000);
        inventory.getInventory().interact("Wield", 1277);
        sleep(Math.abs(randomNumber())+2000);

        inventory.getInventory().interact("Wield", 1171);
        sleep(Math.abs(randomNumber())+2000);

        inventory.getInventory().dropAll();
        sleep(Math.abs(randomNumber())+2000);

        for (int i = 0; i < toChickens.length; i++) {
            getWalking().walk(toChickens[i]);
            sleep(Math.abs(randomNumber())+1500);
        }

        RS2Object gate = getObjects().closest("Gate");

        if(gate.hasAction("Open")) {
            gate.interact("Open");
            sleep(Math.abs(randomNumber())+2000);
        }

    }

    public boolean isReadyToAttack() throws InterruptedException {
        NPC enemy = getNpcs().closest("Chicken");

        if(getCombat().isFighting() || combat.isFighting())
            return false;
        if(myPlayer().isAnimating() || myPlayer().isUnderAttack() || myPlayer().isMoving())
            return false;
        if(getInventory().isFull())
            return false;
        if((enemy == null)){
            return false;
        }
        if(enemy.isUnderAttack() || (enemy.getHealthPercent() <=0) || !map.canReach(enemy)){
            return false;
        }

        return true;
    }

    public void attacking() throws InterruptedException {
        NPC enemy = getNpcs().closest("Chicken");
        if(getCombat().isFighting() || combat.isFighting())
            return;
        if(myPlayer().isAnimating() || myPlayer().isUnderAttack() || myPlayer().isMoving())
            return;
        if(getInventory().isFull())
            return;
        if((enemy == null)){
            return;
        }
        if(enemy.isUnderAttack() || (enemy.getHealthPercent() <=0) || !map.canReach(enemy)){
            return;
        }
        if(enemy.getHealthPercent() > 0) {
            if (enemy.interact("Attack")) {
                sleep(Math.abs(randomNumber())+1000);
                mouse.moveOutsideScreen();
                sleep(7527 + randomNumber());
            }
        }
    }

    public void chickensToGE() throws InterruptedException {
        sleep(Math.abs(randomNumber())+2000);
        RS2Object door = getObjects().closest("Door");

        if(door.hasAction("Open")) {
            door.interact("Open");
            sleep(Math.abs(randomNumber())+5000);
        }
        sleep(Math.abs(randomNumber())+5000);

        for (int i = 0; i < chickensToVarrock.length; i++) {
            getWalking().walk(chickensToVarrock[i]);
            sleep(Math.abs(randomNumber())+1500);
        }
    }

    public void sellAllChicken() throws InterruptedException {
        sleep(Math.abs(randomNumber())+3000);
        NPC clerk = getNpcs().closest(GE_CLERK_ID);
        clerk.interact("Exchange");

        sleep(Math.abs(randomNumber())+3000);
        if (getPlayers().getInventory().contains("Bones")) {
            grandExchange.sellItem(526, 1, 10);
            sleep(Math.abs(randomNumber())+3000);
        }
        if (getPlayers().getInventory().contains("Raw chicken")) {
            grandExchange.sellItem(2138, 1, 10);
            sleep(Math.abs(randomNumber())+3000);
        }
        sleep(Math.abs(randomNumber())+3000);
        grandExchange.collect();
        sleep(Math.abs(randomNumber())+3000);
        grandExchange.collect();
        sleep(Math.abs(randomNumber())+3000);
    }

    public void GEtoVarrock() throws InterruptedException {
        for (int i = 0; i < pathGEtoVarrock.length; i++) {
            getWalking().walk(pathGEtoVarrock[i]);
            sleep(Math.abs(randomNumber())+1500);
        }
    }

    public void hideRoofs() throws InterruptedException {
        sleep(Math.abs(randomNumber())+2000);
        getTabs().open(Tab.SETTINGS);
        sleep(Math.abs(randomNumber())+3000);

        RS2Widget allSettingsButton = getWidgets().get(116, 73,  9);
        allSettingsButton.interact();
        sleep(Math.abs(randomNumber())+3000);

        RS2Widget searchBarClick = getWidgets().get(134, 11);
        searchBarClick.interact();
        sleep(Math.abs(randomNumber())+3000);

        getKeyboard().typeString("roof");
        sleep(Math.abs(randomNumber())+3000);

        RS2Widget hideRoofCheckMark = getWidgets().get(134, 18, 2);
        hideRoofCheckMark.interact();
        sleep(Math.abs(randomNumber())+3000);

        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+2000);
    }

    public void beforeSoundOff() throws InterruptedException {
        sleep(Math.abs(randomNumber())+3000);
        RS2Widget clickNameBox = getWidgets().get(558, 9);
        clickNameBox.interact();
        sleep(Math.abs(randomNumber())+3000);

        try {
            keyboardInput();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        sleep(Math.abs(randomNumber())+4000);
        RS2Widget chooseGivenName = getWidgets().get(558, 16);
        chooseGivenName.interact();

        sleep(Math.abs(randomNumber())+4000);
        RS2Widget setName = getWidgets().get(558, 19, 0);
        setName.interact();

        sleep(Math.abs(randomNumber())+4000);
        charOutfitGeneration();

        sleep(Math.abs(randomNumber())+3000);
        RS2Widget confirm = getWidgets().get(679, 68, 0);
        confirm.interact();

        sleep(Math.abs(randomNumber())+4000);
        NPC firstGuy = getNpcs().closest(FIRSTGUY_ID);
        firstGuy.interact("Talk-to");
        sleep(Math.abs(randomNumber())+5000);

        for(int i = 0; i<6; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+1500);
        }

        dialogues.getDialogues().selectOption(3);
        sleep(Math.abs(randomNumber())+2000);

        for(int i = 0; i<6; i++){
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber())+2000);
        }

        for(int i = 0; i<7; i++) {
            getTabs().open(Tab.SETTINGS);
            sleep(Math.abs(randomNumber())+1000);
        }
    }

    public void charOutfitGeneration() throws InterruptedException {
        for(int i = 0; i < SUBROOT_IDS.length; i++) {
            int num = randomNumberClicksForCharCreation();
            for (int j = 0; j < num; j++) {
                sleep(Math.abs(randomNumber())+500);
                RS2Widget charCreationClicks = getWidgets().get(679, SUBROOT_IDS[i], 9);
                charCreationClicks.interact();
            }
        }
    }

    public int randomNumberClicksForCharCreation(){
        int num = (int)(Math.random()*8) + 1;
        log(num);
        return num;
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

        RS2Widget leftClickOption = getWidgets().get(116, 91);
        leftClickOption.interact();
        sleep(Math.abs(randomNumber())+3000);

        RS2Widget zoom = getWidgets().get(116, 99);
        zoom.interact();
        sleep(Math.abs(randomNumber())+2000);

        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+2000);
    }

    public void setToFixedLayout() throws InterruptedException {
        sleep(Math.abs(randomNumber())+2000);
        getTabs().open(Tab.SETTINGS);
        sleep(Math.abs(randomNumber())+2000);

        RS2Widget displaySettings = getWidgets().get(116, 116);
        displaySettings.interact();
        sleep(Math.abs(randomNumber())+2000);

        RS2Widget downArrow = getWidgets().get(116, 25, 3);
        downArrow.interact();
        sleep(Math.abs(randomNumber())+2000);

        RS2Widget fixedOption = getWidgets().get(116, 82, 1);
        fixedOption.interact();
        sleep(Math.abs(randomNumber())+2000);

        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber())+2000);
    }

    public void firstTimeBankTutorial() throws InterruptedException {
        sleep(Math.abs(randomNumber())+2000);
        NPC bankNPC = getNpcs().closest(BANK_CLERK_ID);
        bankNPC.interact("Bank");
        sleep(Math.abs(randomNumber())+2000);
        stop();
        RS2Widget tutorialXButton = getWidgets().get(664, 29);
        tutorialXButton.interact();

        sleep(Math.abs(randomNumber())+2000);
        RS2Widget bankXButton = getWidgets().get(12, 2, 11);
        bankXButton.interact();
        sleep(Math.abs(randomNumber())+2000);
        stop();

        for (int i = 0; i < firstClickOutOfBank.length; i++) {
            getWalking().walk(firstClickOutOfBank[i]);
            sleep(Math.abs(randomNumber())+2000);
        }
    }

    public void turnSoundOff() throws InterruptedException {
        sleep(Math.abs(randomNumber())+2000);
        getTabs().open(Tab.SETTINGS);
        sleep(Math.abs(randomNumber())+3000);

        RS2Widget audioSettings = getWidgets().get(116, 109);
        audioSettings.interact();
        sleep(Math.abs(randomNumber())+3000);

        RS2Widget musicOff = getWidgets().get(116, 28, 0);
        musicOff.interact();
        sleep(Math.abs(randomNumber())+2000);

        RS2Widget soundEffectsOff = getWidgets().get(116, 42, 0);
        soundEffectsOff.interact();
        sleep(Math.abs(randomNumber())+2000);

        RS2Widget areaSoundOff = getWidgets().get(116, 56, 0);
        areaSoundOff.interact();
        sleep(Math.abs(randomNumber())+2000);
    }
    public void firstClickOffTutorialIsland() throws InterruptedException {
        sleep(Math.abs(randomNumber())+2000);
        for (int i = 0; i < firstClick.length; i++) {
            getWalking().walk(firstClick[i]);
            sleep(Math.abs(randomNumber())+1000);
        }
    }

}