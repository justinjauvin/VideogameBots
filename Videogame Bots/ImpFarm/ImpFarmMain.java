package ImpFarm;

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

@ScriptManifest(info = "Imp Farmer", logo = "", version = 1.0, author = "jbird", name = "Imp Killer")

public class ImpFarmMain extends Script {
    private final int FOOD_ID = 379;
    private final int[] ENEMY_ID = {5007};
    private final int GE_CLERK_ID = 2149;
    private final int BANK_CLERK_ID = 1634;
    private final int SEAMAN_ID = 3645;
    private final int SCORP_ID = 3024;
    private final int SKELETON_ID = 3024;
    private final String[] lootList = {"Black bead", "Yellow bead", "White bead", "Red bead", "Mind talisman"};
    GroundItem loot;
    NPC clerk;
    NPC bankNPC;
    NPC seaman;
    private Position[] pathToVarrock = {
            new Position(3196, 3430, 0), new Position(3193, 3448, 0),
            new Position(3177, 3458, 0), new Position(3167, 3472, 0),
            new Position(3164, 3483, 0), new Position(3164, 3488, 0)
    };
    private Position[] pathToKaramja = {
            new Position(3234, 3222, 0),
            new Position(3231, 3231, 0), new Position(3222, 3240, 0),
            new Position(3209, 3239, 0), new Position(3195, 3238, 0),
            new Position(3178, 3250, 0), new Position(3161, 3260, 0),
            new Position(3156, 3259, 0),
            new Position(3134, 3261, 0), new Position(3143, 3265, 0),
            new Position(3138, 3264, 0),
            new Position(3118, 3263, 0), new Position(3107, 3266, 0),
            new Position(3094, 3278, 0), new Position(3077, 3277, 0),
            new Position(3059, 3265, 0), new Position(3044, 3246, 0),
            new Position(3033, 3235, 0), new Position(3027, 3222, 0),
            new Position(3028, 3216, 0)
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
    private Position[] backToImpSpot = {
            new Position(2832, 3179, 0)
    };

    @Override
    public int onLoop() throws InterruptedException {
        //**make more things into widgets if they stop working i.e. grandexchange.collect()
/*
        while(!isReadyForTrip()){
            checkIfRightAmountSupplies();
            sleep(Math.abs(randomNumber() + 1000));
        }
*/

        setFireStrike();
        sleep(Math.abs(randomNumber() + 1000));

        homeTele();
        sleep(Math.abs(randomNumber() + 15000));

        sleep(Math.abs(randomNumber() + 1000));
        runEnergy();
        sleep(Math.abs(randomNumber() + 1000));

        LumbridgetoKaramja();
        sleep(Math.abs(randomNumber() + 1000));

        karamjaPortToImpSpot();
        sleep(Math.abs(randomNumber() + 1000));

        while (!getInventory().isFull()) {
            if (lootExists()) {
                loot();
            }
            else if (isReadyToAttack()) {
                if (!getInventory().isFull())
                    attack();
            }
            else if (climbRopeIfItExists());

            else if (myPlayer().isUnderAttack()) {
                NPC scorp = getNpcs().closest(SCORP_ID);
                if (scorp != null) {
                    if (scorp.interact("Attack")) {
                        clickRunEnergy();
                        for (int i = 0; i < backToImpSpot.length; i++) {
                            getWalking().walk(backToImpSpot[i]);
                            sleep(Math.abs(randomNumber() + 1500));
                        }
                    }
                }
                else if (scorp == null) {
                    NPC skeleton = getNpcs().closest("Skeleton");
                    if (skeleton != null) {
                        clickRunEnergy();
                        for (int i = 0; i < backToImpSpot.length; i++) {
                            getWalking().walk(backToImpSpot[i]);
                            sleep(Math.abs(randomNumber() + 1500));
                        }
                    }
                }
            }
        }

        sleep(Math.abs(randomNumber() + 1000));
        teleOut();
        sleep(Math.abs(randomNumber() + 1000));

        varrockToGE();
        sleep(Math.abs(randomNumber() + 2000));

        sellAll();
        sleep(Math.abs(randomNumber() + 2000));

        buySupplies();
        sleep(Math.abs(randomNumber() + 2000));

        return 0;
    }

    public boolean hasFood() {
        return getInventory().contains(FOOD_ID);
    }

    public int randomNumber() {
        double variance = (Math.random() * 6967);
        int num = (int) (Math.random() * variance) + 1;
        if (num % 2 == 0)
            num *= -1;

        log("Variance: " + variance);
        log("Sleep Variance: " + num);

        return num;
    }

    public int getHpPercent() {
        log("getHpPercent()");

        float staticHp = getSkills().getStatic(Skill.HITPOINTS);
        float dynamicHp = getSkills().getDynamic(Skill.HITPOINTS);

        return (int) (100 * (dynamicHp / staticHp));
    }

    public boolean hasRunes() {
        if (getInventory().contains("Air rune") && getInventory().getAmount("Air rune") >= 10 && getInventory().contains("Mind rune")) {
            log("Has Runes");
            return true;
        } else {
            log("No Runes");
            return false;
        }
    }

    private boolean lootExists() throws InterruptedException {
        for (int i = 0; i < lootList.length; i++) {
            loot = groundItems.closest(lootList[i]);
            if (loot != null && loot.exists() && loot.isVisible()) {
                sleep(Math.abs(randomNumber()) + 2500);
                log("Loot exists!");
                return true;
            }
        }
        return false;
    }

    private void loot() throws InterruptedException {
        for (int i = 0; i < lootList.length; i++) {
            loot = groundItems.closest(lootList[i]);
            if (loot != null && loot.exists() && getMap().canReach(loot) && loot.isVisible()) {
                log("loot: " + loot);
                loot.interact("Take");
                sleep(Math.abs(randomNumber()) + 3500);
                if (loot.interact("Take"))
                    sleep(Math.abs(randomNumber()) + 3500);
                log("Taking loot");

            }
        }
    }

    public void heal() throws InterruptedException {
        sleep(randomNumber());
        getInventory().getItem(FOOD_ID).interact("Eat");
    }

    public boolean isReadyToAttack() throws InterruptedException {
        NPC enemy = getNpcs().closest(ENEMY_ID);
        //if(!hasFood())
        //   return false;
        if (!hasRunes()) {
            log("Is not1 Ready to attack");
            return false;
        }
        if (getCombat().isFighting() || combat.isFighting()) {
            log("Is not2 Ready to attack");
            sleep(Math.abs(randomNumber()) + 2000);
            return false;
        }
        if (myPlayer().isAnimating() || myPlayer().isUnderAttack() || myPlayer().isMoving()) {
            log("Is not3 Ready to attack");
            return false;
        }
        if (getInventory().isFull()) {
            log("Is not4 Ready to attack");
            return false;
        }
        if ((enemy == null)) {
            log("Is not5 Ready to attack");
            for (int i = 0; i < backToImpSpot.length; i++) {
                getWalking().walk(backToImpSpot[i]);
                sleep(Math.abs(randomNumber() + 5000));
            }
            return false;
        }
        if (enemy.isUnderAttack() || !map.canReach(enemy)) {
            for (int i = 0; i < backToImpSpot.length; i++) {
                getWalking().walk(backToImpSpot[i]);
                sleep(Math.abs(randomNumber() + 5000));
            }
            log("Is not6 Ready to attack");
            return false;
        }

        return true;
    }

    public void attack() throws InterruptedException {
        log("Sleep Before Moving off Screen: " + Math.abs(randomNumber()));
        NPC enemy = getNpcs().closest(ENEMY_ID);
        if (!isReadyToAttack())
            return;

        //if (getHpPercent() < 1)
        //heal();

        if(enemy.getHealthPercent() > 0) {
            if(enemy != null) {
                enemy.interact("Attack");
                sleep(Math.abs(randomNumber()) + 1000);
                mouse.moveOutsideScreen();
                sleep(Math.abs(randomNumber() + 8761));
            }
        }
        else {
            for (int i = 0; backToImpSpot.length > i; i++) {
                getWalking().walk(backToImpSpot[i]);
                sleep(Math.abs(randomNumber()));
            }
        }
    }

    public void teleOut() throws InterruptedException {
        getTabs().open(Tab.MAGIC);
        sleep(1000 + randomNumber());
        getMagic().castSpell(Spells.NormalSpells.VARROCK_TELEPORT);
        sleep(500 + randomNumber());
        getTabs().open(Tab.INVENTORY);
        sleep(1000 + randomNumber());
    }

    public void varrockToGE() throws InterruptedException {
        for (int i = 0; i < pathToVarrock.length; i++) {
            getWalking().walk(pathToVarrock[i]);
            sleep(Math.abs(randomNumber() + 1500));
        }
    }

    public void sellAll() throws InterruptedException {
        clerk = getNpcs().closest(GE_CLERK_ID);
        clerk.interact("Exchange");

        sleep(1000 + randomNumber());
        grandExchange.collect();
        sleep(3000 + randomNumber());
        sleep(1000 + randomNumber());
        grandExchange.collect();
        sleep(3000 + randomNumber());

        if (getPlayers().getInventory().contains("Mind talisman")) {
            while (getPlayers().getInventory().contains("Mind talisman")) {
                grandExchange.sellItem(1448, 700, 28);
                sleep(2000 + randomNumber());
            }
        }

        if (getPlayers().getInventory().contains("Black bead")) {
            while(getPlayers().getInventory().contains("Black bead")){
                grandExchange.sellItem(1474, 700, 28);
                sleep(3000 + randomNumber());
            }
        }
        sleep(2000 + randomNumber());
        grandExchange.collect();
        sleep(3000 + randomNumber());
        grandExchange.collect();
        sleep(3000 + randomNumber());

        if (getPlayers().getInventory().contains("Red bead")) {
            while(getPlayers().getInventory().contains("Red bead")) {
                grandExchange.sellItem(1470, 700, 28);
                sleep(2000 + randomNumber());
            }
        }
        if (getPlayers().getInventory().contains("Yellow bead")) {
            while(getPlayers().getInventory().contains("Yellow bead")) {
                grandExchange.sellItem(1472, 700, 28);
                sleep(2000 + randomNumber());
            }
        }
        if (getPlayers().getInventory().contains("White bead")) {
            while(getPlayers().getInventory().contains("White bead")) {
                grandExchange.sellItem(1476, 700, 28);
                sleep(2000 + randomNumber());
            }
        }
        sleep(1000 + randomNumber());
        grandExchange.collect();
        sleep(3000 + randomNumber());
        grandExchange.collect();
    }

    public void checkIfRightAmountSupplies() throws InterruptedException {
        if (!getInventory().contains("Law rune")) {
            sleep(Math.abs(randomNumber() + 1000));
            bankNPC = getNpcs().closest(BANK_CLERK_ID);
            bankNPC.interact("Bank");
            sleep(Math.abs(randomNumber() + 1000));

            bank.getBank().depositAll();
            sleep(Math.abs(randomNumber() + 1000));

            //get money
            if (getBank().contains(995)) {
                sleep(Math.abs(randomNumber() + 500));
                getBank().withdraw(995, 10000);
                sleep(Math.abs(randomNumber() + 1000));
            }

            if (getBank().contains(558)) {
                sleep(Math.abs(randomNumber() + 500));
                getBank().withdraw(558, 500);
            }

            if (getBank().contains(556)) {
                sleep(Math.abs(randomNumber() + 500));
                getBank().withdraw(556, 1000);
            }

            sleep(Math.abs(randomNumber() + 1000));
            clerk = getNpcs().closest(GE_CLERK_ID);
            clerk.interact("Exchange");
            sleep(Math.abs(randomNumber() + 1000));
            grandExchange.buyItem(563, "Law rune", 500, 1);
            sleep(Math.abs(randomNumber() + 1000));
            grandExchange.collect();
            sleep(Math.abs(randomNumber() + 3000));
        }

        if (!getInventory().contains("Mind rune") || getInventory().getAmount("Mind rune") <= 499) {
            sleep(Math.abs(randomNumber() + 500));
            bankNPC = getNpcs().closest(BANK_CLERK_ID);
            bankNPC.interact("Bank");
            sleep(Math.abs(randomNumber() + 1000));

            bank.getBank().depositAll();
            sleep(Math.abs(randomNumber() + 1000));

            //get money
            if (getBank().contains(995)) {
                sleep(Math.abs(randomNumber() + 1000));
                getBank().withdraw(995, 10000);
            }

            if (getBank().contains(558)) {
                sleep(Math.abs(randomNumber() + 500));
                getBank().withdraw(558, 500);
            }

            if (getBank().contains(556)) {
                sleep(Math.abs(randomNumber() + 500));
                getBank().withdraw(556, 1000);
            }

            sleep(Math.abs(randomNumber() + 1000));
            clerk = getNpcs().closest(GE_CLERK_ID);
            clerk.interact("Exchange");
            sleep(Math.abs(randomNumber() + 1000));
            grandExchange.buyItem(558, "Mind rune", 5, 500);
            sleep(1000 + randomNumber());
            grandExchange.collect();
            sleep(3000 + randomNumber());
        }

        if (!getInventory().contains("Air rune") || getInventory().getAmount("Air rune") <= 999) {
            sleep(Math.abs(randomNumber() + 500));
            bankNPC = getNpcs().closest(BANK_CLERK_ID);
            bankNPC.interact("Bank");
            sleep(Math.abs(randomNumber() + 1000));

            bank.getBank().depositAll();
            sleep(Math.abs(randomNumber() + 1000));

            //get money
            if (getBank().contains(995)) {
                sleep(Math.abs(randomNumber() + 500));
                getBank().withdraw(995, 10000);
            }

            if (getBank().contains(558)) {
                sleep(Math.abs(randomNumber() + 500));
                getBank().withdraw(558, 500);
            }

            if (getBank().contains(556)) {
                sleep(Math.abs(randomNumber() + 500));
                getBank().withdraw(556, 1000);
            }

            sleep(Math.abs(randomNumber() + 1000));
            clerk = getNpcs().closest(GE_CLERK_ID);
            clerk.interact("Exchange");
            sleep(Math.abs(randomNumber() + 500));
            grandExchange.buyItem(556, "Air rune", 7, 1000);
            sleep(1000 + randomNumber());
            grandExchange.collect();
            sleep(3000 + randomNumber());
        }
        bankAndSupplies();
    }

    public void bankAndSupplies() throws InterruptedException {
        sleep(Math.abs(randomNumber() + 1000));
        bankNPC = getNpcs().closest(BANK_CLERK_ID);
        bankNPC.interact("Bank");

        sleep(Math.abs(randomNumber() + 2000));
        bank.getBank().depositAll();

        sleep(Math.abs(randomNumber() + 2000));
        bank.getBank().depositAll();

        if (getBank().contains(995)) {
            sleep(Math.abs(randomNumber() + 500));
            getBank().withdraw(995, 30);
        }
        if (getBank().contains(563)) {
            sleep(Math.abs(randomNumber() + 500));
            getBank().withdraw(563, 1);
        }
        if (getBank().contains(558)) {
            sleep(Math.abs(randomNumber() + 500));
            getBank().withdraw(558, 500);
        }
        if (getBank().contains(556)) {
            sleep(Math.abs(randomNumber() + 500));
            getBank().withdraw(556, 1000);
        }
    }

    public boolean isReadyForTrip() {
        if (getPlayers().getInventory().contains("Law rune") &&
                getInventory().contains("Mind rune") && getInventory().getAmount("Mind rune") >= 499 &&
                getInventory().contains("Air rune") && getInventory().getAmount("Air rune") >= 999 &&
                getInventory().contains("Coins") && getInventory().getAmount("Coins") >= 30) {
            return true;
        } else {
            return false;
        }
    }

    public void homeTele() throws InterruptedException {
        sleep(1000 + randomNumber());
        getTabs().open(Tab.MAGIC);
        sleep(1000 + randomNumber());
        getMagic().castSpell(Spells.NormalSpells.HOME_TELEPORT);
        sleep(500 + randomNumber());
        getTabs().open(Tab.INVENTORY);
    }

    public void LumbridgetoKaramja() throws InterruptedException {
        sleep(Math.abs(randomNumber() + 1000));
        for (int i = 0; i < pathToKaramja.length; i++) {
            getWalking().walk(pathToKaramja[i]);
            sleep(Math.abs(randomNumber()) + 1500);
        }

        sleep(Math.abs(randomNumber()) + 6000);
        seaman = getNpcs().closest(SEAMAN_ID);
        seaman.interact("Pay-fare");
        sleep(Math.abs(randomNumber()) + 7000);

        for(int i = 0; i < 3; i++) {
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber()) + 2000);
        }
        dialogues.getDialogues().selectOption(1);
        sleep(Math.abs(randomNumber()) + 3000);
        dialogues.getDialogues().selectOption(1);
        sleep(Math.abs(randomNumber()) + 3000);

        for(int i = 0; i < 3; i++) {
            dialogues.clickContinue();
            sleep(Math.abs(randomNumber()) + 15000);
        }
        objects.closest("Gangplank").interact("Cross");
        sleep(Math.abs(randomNumber()) + 4000);
    }

    public void karamjaPortToImpSpot() throws InterruptedException {
        for (int i = 0; i < pathToImpSpot.length; i++) {
            getWalking().walk(pathToImpSpot[i]);
            sleep(Math.abs(randomNumber() + 1500));
        }
    }

    public void setFireStrike() throws InterruptedException {
        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber()) + 2000);

        if (getInventory().contains("Staff of fire")) {
            getInventory().interact("Wield", 1387);
        }
        sleep(Math.abs(randomNumber()) + 2000);

        getTabs().open(Tab.ATTACK);
        sleep(Math.abs(randomNumber()) + 2000);


        RS2Widget styleMagic = getWidgets().get(593, 26, 4);
        styleMagic.interact();
        sleep(Math.abs(randomNumber()) + 2000);


        RS2Widget chooseFireStrike = getWidgets().get(201, 1, 4);
        chooseFireStrike.interact();
        sleep(Math.abs(randomNumber()) + 2000);

        getTabs().open(Tab.INVENTORY);
        sleep(Math.abs(randomNumber()) + 2000);
    }

    public void buySupplies() throws InterruptedException {
        clerk = getNpcs().closest(GE_CLERK_ID);
        clerk.interact("Exchange");

        sleep(Math.abs(randomNumber()) + 2000);
        grandExchange.collect();
        sleep(Math.abs(randomNumber()) + 2000);

        sleep(Math.abs(randomNumber()) + 2000);
        grandExchange.collect();
        sleep(Math.abs(randomNumber()) + 2000);

        sleep(Math.abs(randomNumber()) + 2000);
        grandExchange.buyItem(563, "Law rune", 500, 1);

        if (getInventory().getAmount("Mind rune") < 500) {
            sleep(Math.abs(randomNumber()) + 2000);
            grandExchange.buyItem(558, "Mind rune", 5, 550);
        }
        sleep(Math.abs(randomNumber()) + 2000);
        grandExchange.collect();
        sleep(Math.abs(randomNumber()) + 2000);
        if (getInventory().getAmount("Air rune") < 1000) {
            sleep(Math.abs(randomNumber()) + 1000);
            grandExchange.buyItem(556, "Air rune", 7, 1100);
        }
        sleep(Math.abs(randomNumber()) + 2000);
        grandExchange.collect();
        sleep(Math.abs(randomNumber()) + 2000);

        sleep(Math.abs(randomNumber()) + 2000);
        grandExchange.collect();
        sleep(Math.abs(randomNumber()) + 2000);
    }

    public void runEnergy() throws InterruptedException {
        int num = (int) (Math.random() * 12) + 1;
        log("Random run energy number: " + num);

        sleep(Math.abs(randomNumber() + 1000));

        if (settings.getSettings().getRunEnergy() == 100) {
            RS2Widget toggleRun = getWidgets().get(160, 23);
            toggleRun.interact("Toggle Run");
        }

    }

    public void clickRunEnergy() throws InterruptedException {
        RS2Widget toggleRun = getWidgets().get(160, 23);
        toggleRun.interact("Toggle Run");
        sleep(Math.abs(randomNumber() / 2));
    }

    public boolean climbRopeIfItExists() throws InterruptedException {
        RS2Object rope = getObjects().closest(18969);
        if (rope != null) {
            while (rope.hasAction("Climb")) {
                rope.interact("Climb");
                sleep(Math.abs(randomNumber()) + 5000);
                for (int i = 0; i < backToImpSpot.length; i++) {
                    getWalking().walk(backToImpSpot[i]);
                    sleep(Math.abs(randomNumber()) + 1500);
                }
                return true;
            }
        }
        return false;
    }
}