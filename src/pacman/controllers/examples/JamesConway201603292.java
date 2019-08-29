package pacman.controllers.examples;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.Random;

public class JamesConway201603292 extends Controller<Constants.MOVE> {

    long timeDue;

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        this.timeDue = timeDue;
        return rollout(game);
    }

    public Constants.MOVE rollout(Game game){
        Constants.MOVE bestMove = null;
        int highestScore = 0, score;
        //for each possible move, make the move then do random rollout
        for(Constants.MOVE move :game.getPossibleMoves(game.getPacmanCurrentNodeIndex())){
            Game gameCopy = game.copy();
            gameCopy.advanceGame(move, new Legacy().getMove(gameCopy, timeDue));
            score = randomRollout(gameCopy);
            //find best average score from each possible move
            if(score > highestScore){
                highestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    public int randomRollout(Game game){
        int totalScore = 0, averageScore;
        int timesRun = 70;
        //do random rollout 50 times
        for(int i=0; i<timesRun; i++) {
            Game gameCopy = game.copy();
            while (!gameCopy.wasPacManEaten()) {
                Constants.MOVE move = getRandomMove(gameCopy);
                gameCopy.advanceGame(move, new Legacy().getMove(gameCopy, timeDue));
            }
            totalScore += gameCopy.getScore();
        }
        averageScore = (totalScore /timesRun);
        return averageScore;
    }

    private Constants.MOVE getRandomMove(Game state){
        Random rng = new Random();
        int random = (rng.nextInt(state.getPossibleMoves(state.getPacmanCurrentNodeIndex(), state.getPacmanLastMoveMade()).length));
        return state.getPossibleMoves(state.getPacmanCurrentNodeIndex(), state.getPacmanLastMoveMade())[random];
    }
}
