import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

/**
 * A Greep is an alien creature that likes to collect tomatoes.
 * 
 * Rules:
 * 
 * Rule 1 
 * Only change the class 'MyGreep'. No other classes may be modified or created. 
 *
 * Rule 2 
 * You cannot extend the Greeps' memory. That is: you are not allowed to add 
 * fields (other than final fields) to the class. Some general purpose memory is
 * provided. (The ship can also store data.) 
 * 
 * Rule 3 
 * You can call any method defined in the "Greep" superclass, except act(). 
 * 
 * Rule 4 
 * Greeps have natural GPS sensitivity. You can call getX()/getY() on any object
 * and get/setRotation() on yourself any time. Friendly greeps can communicate. 
 * You can call getMemory() and getFlag() on another greep to ask what they know. 
 * 
 * Rule 5 
 * No creation of objects. You are not allowed to create any scenario objects 
 * (instances of user-defined classes, such as MyGreep). Greeps have no magic 
 * powers - they cannot create things out of nothing. 
 * 
 * Rule 6 
 * You are not allowed to call any methods (other than those listed in Rule 4)
 * of any other class in this scenario (including Actor and World). 
 *  
 * If you change the name of this class you should also change it in
 * Ship.createGreep().
 * 
 * Please do not publish your solution anywhere. We might want to run this
 * competition again, or it might be used by teachers to run in a class, and
 * that would be ruined if solutions were available.
 * 
 * 
 * @author (your name here)
 * @version 0.1
 */
public class MyGreep extends Greep
{
    // Remember: you cannot extend the Greep's memory. So:
    // no additional fields (other than final fields) allowed in this class!
   
    private static final int TOMATO_LOCATION_KNOWN = 1;
    
    /**
     * Default constructor. Do not remove.
     */
    public MyGreep(Ship ship)
    {
        super(ship);
        setMemory(3, -1);
    }
    
    /**
     * Do what a greep's gotta do.
     */
    public void act()
    {
        super.act();   // do not delete! leave as first statement in act().
        
        checkFood(); //check for the tomato first
        
        if (carryingTomato()) {
            if (atWater()) {
                int r = getRotation();
                setRotation (r + Greenfoot.getRandomNumber(80) + 60);
                move();
            }
            else {
                bringTomatoHome();
            }
        }
        
        else if (getTomatoes() != null) {
            TomatoPile tomatoes = getTomatoes();
            if (!blockAtPile(tomatoes)) { //if there is no block on the pile, go to the centre of the pile.
                turnTowards(tomatoes.getX(), tomatoes.getY());
                move();
            }
        }
        
        else if (getMemory(0) == TOMATO_LOCATION_KNOWN) {
            turnTowards(getMemory(1), getMemory(2));
            move();
            if (Math.abs(getMemory(1) - getX()) < 5 && Math.abs( getMemory(2) - getY()) < 5){
                if (getTomatoes() == null){
                    setMemory(0, TOMATO_LOCATION_KNOWN - 1);
                }
            }
        }
        
        else if (numberOfOpponents(false) > 3) { //cowardness 
            kablam();
        }
        
        else {
            randomWalk();
        }
        
        if (atWater() || moveWasBlocked()) {
            int r = getRotation();
            setRotation (r + Greenfoot.getRandomNumber(2) * 180 - 90);
            move();
        }
    }
    
    /** 
     * Move forward, with a slight chance of turning randomly
     */
    public void randomWalk()
    {
        // there's a 3% chance that we randomly turn a little off course
        if (randomChance(3)) {
            turn((Greenfoot.getRandomNumber(3) - 1) * 100);
        }
        
        move();
    }

    /**
     * Is there any food here where we are? If so, try to load some!
     */
    public void checkFood()
    {
        // check whether there's a tomato pile here
        TomatoPile tomatoes = getTomatoes();
        if(tomatoes != null) {
            loadTomato();
            // Note: this attempts to load a tomato onto *another* Greep. It won't
            // do anything if we are alone here.
            setMemory(0, TOMATO_LOCATION_KNOWN);
            setMemory(1, tomatoes.getX());
            setMemory(2, tomatoes.getY());
        }
    }

    /**
     * This method specifies the name of the greeps (for display on the result board).
     * Try to keep the name short so that it displays nicely on the result board.
     */
    public String getName()
    {
        return "CS18010";  // write your name here!
    }
    
    /**
     * Get back to the spaceship!
     */
    private void bringTomatoHome() {
        if (atShip()) {
            dropTomato();
        }
        else {
            int r = getRotation();
            turnHome();
            int r2 = getRotation();
            if ( Math.abs(r - r2) < 180){
                setRotation((r + r2) / 2);
            }else{
                //setRotation((r + r2 - 180) / 2);
                turnHome();
            }
            move();
        }
            
    }

    private int getNumber(){
        int data[] = getShipData();
        int number = 0;
        if (getMemory(3) == -1){
            for (int i = 10; i < 991; i+=10){
                if (data[i] == 0){
                    number = i;
                    data[i] = number;
                    setMemory(3, number);
                    return number;
                }
            }
        }
        return getMemory(3);
    }
    

    /**
     * If the grep ends up at a pile where there are no friends blocking, the grep will block the pile of tomatoes
     */
    private boolean blockAtPile(TomatoPile tomatoes) {
        boolean atPileCentre = tomatoes != null && distanceTo(tomatoes.getX(), tomatoes.getY()) < 4;
        if(atPileCentre && getFriend() == null ) {
            block();
            return true;
        }
        else {
            return false;
        }
    }
    
        private int distanceTo(int x, int y)
    {
        int deltaX = getX() - x;
        int deltaY = getY() - y;
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
