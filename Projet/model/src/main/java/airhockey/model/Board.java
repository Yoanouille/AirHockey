package airhockey.model;

public class Board {

    private Palet palet;
    private Pusher[] pushers;
    private Wall[] walls;
    private Goal[] goals;

    private final int WIDTH = 800;
    private final int HEIGHT = 500;
    private final int GOAL_SIZE = 125;


    public Board() {
        palet = new Palet(new Vector(400, 250), 20);
        palet.setSpeed(new Vector(0, 0));

        pushers = new Pusher[2];
        pushers[0] = new Pusher(new Vector(300, 250), 25);
        pushers[1] = new Pusher(new Vector(600, 250), 25);

        walls = new Wall[6];
        walls[0] = new Wall(50, 50, WIDTH-100, 0); //mur haut
        walls[1] = new Wall(50, 50, 0, ((HEIGHT-100)/2)-(GOAL_SIZE/2)); //mur gauche 1
        walls[2] = new Wall(50,50+((HEIGHT-100)/2)+(GOAL_SIZE/2),0, ((HEIGHT-100)/2)-(GOAL_SIZE/2)); //mur gauche 2
        walls[3] = new Wall(WIDTH-50, 50, 0, ((HEIGHT-100)/2)-(GOAL_SIZE/2)); //mur droite 1
        walls[4] = new Wall(WIDTH-50, 50+((HEIGHT-100)/2)+(GOAL_SIZE/2), 0, ((HEIGHT-100)/2)-(GOAL_SIZE/2)); //mur droite 2
        walls[5] = new Wall(50, HEIGHT-50, WIDTH-100, 0); //mur bas
        
        goals = new Goal[2];
        goals[0] = new Goal(50-60, 50+((HEIGHT-100)/2)-(GOAL_SIZE/2),60,GOAL_SIZE); //goal gauche
        goals[1] = new Goal(WIDTH-50,50+((HEIGHT-100)/2)-(GOAL_SIZE/2),60,GOAL_SIZE); // goal droite
    }

    public Pusher[] getPushers() {
        return pushers;
    }

    public Wall[] getWalls() {
        return walls;
    }

    public Goal[] getGoals(){
        return goals;
    }

    public Palet getPalet() {
        return palet;
    }

    public void setPalet(Palet p){
        palet = p;
    }


    public void update(double dt){
        palet.update(dt, walls, pushers);
        pushers[0].resetMovement();
        pushers[1].resetMovement();
    }
}
