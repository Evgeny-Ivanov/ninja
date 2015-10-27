package game;

/**
 * Created by ilya on 27.10.15.
 */
public interface GameMechanics {
    public void addUser(String user);

    public void incrementScore(String userName);

    public void run();
}
