package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Set;
import java.util.Stack;
import java.util.Collections;
import java.util.HashSet;

import java.util.Comparator;

public class Solver {

    Stack<WorldState> moveRoute = new Stack<>();
    SearchNode finalNode;

    private class SearchNode{
        WorldState state;
        int moveNum;
        Integer priority;
        SearchNode parent;

        private  SearchNode(WorldState state, SearchNode parent){
            this.state = state;
            this.moveNum = parent == null ? 0 : parent.moveNum + 1;
            priority = state.estimatedDistanceToGoal() + moveNum;
            this.parent = parent;

        }

    }

    private class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode left, SearchNode right) {
            return left.priority.compareTo(right.priority);
        }
    }


    public Solver(WorldState initial){
        //MinPQ priorityQuene = new MinPQ(SearchNodeComparator<SearchNode>);
        MinPQ<SearchNode> pq = new MinPQ<>(new SearchNodeComparator());
        //SearchNodeComparator is classes, not methods, add new
        SearchNode currentNode = new SearchNode(initial, null);
        //pq.insert(currentNode);
        //moveRoute.push(currentNode.state);
        while (!currentNode.state.isGoal()) {
            for(WorldState nextState : currentNode.state.neighbors()) {
                if (currentNode.parent == null || !nextState.equals(currentNode.parent.state)) {
                    pq.insert(new SearchNode(nextState, currentNode));
                }
            }
            currentNode = pq.delMin();
            //.push(currentNode.state);
        }
        finalNode = currentNode;
    }

    public int moves() {
        return finalNode.moveNum;
    }

    public Iterable<WorldState> solution() {
        SearchNode node = finalNode;
        while(node != null){
            moveRoute.push(node.state);
            node = node.parent;
        }


        return moveRoute;
    }



}
