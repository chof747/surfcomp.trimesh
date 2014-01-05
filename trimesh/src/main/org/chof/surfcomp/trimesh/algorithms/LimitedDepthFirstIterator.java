package org.chof.surfcomp.trimesh.algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

/**
 * Extends the DepthFirstIterator from the JGraphT library in a way that it
 * limits the traversal of a graph only up to the given cutoff parameter.
 * <p>
 * Furthermore while traversing the graph it collects a list of the vertices 
 * just behind the cutoff and the edges, which are crossing the border. Thus
 * the class can provide a set of vertices inside and outside the border 
 * provided by the cutoff.
 * 
 * @author chof
 *
 * @param <V>
 * @param <E>
 */
public class LimitedDepthFirstIterator<V, E> extends DepthFirstIterator<V, E> {
	
	private WeightedGraph<V, E> weightedGraph;
	private HashMap<V, Double> distanceMap;
	
	private HashMap<V,E> border;
	
	private double cutoff = 0.0;
	

	/**
	 * Default constructor would construct an iterator for the given graph
	 * starting from an arbitrary vertex and delivering a traversal with 
	 * cutoff 0.0
	 * @see #LimitedDepthFirstIterator(WeightedGraph, Object, double)
	 */
	public LimitedDepthFirstIterator(WeightedGraph<V, E> g) {
		this(g, 0.0);
	}

	/**
	 * Constructor creating an iterator with the provided cutoff but an arbitrary
	 * starting vertex
	 * @see #LimitedDepthFirstIterator(WeightedGraph, Object, double)
	 */
	public LimitedDepthFirstIterator(WeightedGraph<V, E> g,
			double cutoff) {
		this(g, null, cutoff);
	}

	/**
	 * Constructor creating an iterator with the provided cutoff and starting vertex
	 * for the graph
	 * @param g a weighted graph 
	 * @param startVertex the specific startVertex (use null for an arbitrary vertex)
	 * @param cutoff the cutoff value for the sum of weights along a path
	 */
    public LimitedDepthFirstIterator(WeightedGraph<V, E> g, V startVertex, double cutoff) {
    	super(g, startVertex);
		
    	weightedGraph = g;
		distanceMap = new HashMap<V, Double>();
		border = new HashMap<V,E>();
		
		this.cutoff = cutoff;
	}

    /**
     * {@link DepthFirstIterator.encounterVertex(V, E)}
     * <p>
     * Modified encounterVertex routine which calculates the
     * weight of the path from the start vertex to this vertex and 
     * only hands over the vertex to the inherited routine if it lies within the 
     * cutoff.</p>
     * <p>
     * If the vertex lies beyond the cutoff, it is added to the border set.
     * If it lies within the cutoff it is removed from the border set if present.</p>
     */
	protected void encounterVertex(V vertex, E edge)
    {
		double distance = 0.0;
		if (edge != null) {
			V origin = Graphs.getOppositeVertex(weightedGraph, edge, vertex);
			distance = weightedGraph.getEdgeWeight(edge) + distanceMap.get(origin);
		} else {
			distance = 0;
		}
    	
		distanceMap.put(vertex, distance);

    	if (distance > cutoff) {
    		border.put(vertex,edge);
    	} else {
    		border.remove(vertex);
    		super.encounterVertex(vertex, edge);
    	}
    }
	
	/**
	 * clears the border and makes a complete traversal of the graph.
	 * Traverse is necessary to get access to the border vertices
	 * 
	 * @see #getInsideBorder()
	 * @see #getOutsideBorder()
	 * @see #getCompleteBorder()
	 */
	public void traverse() {
		border.clear();
		
		for (LimitedDepthFirstIterator<V, E> i = this; i.hasNext();) {
			i.next();
		}
	}

	/**
	 * @return a set of vertices which lay just within the cutoff distance
	 */
	public Set<V> getInsideBorder() {
		Set<V> inside = new HashSet<V>();
		for (V v : border.keySet()) {
			inside.add(Graphs.getOppositeVertex(weightedGraph, border.get(v), v));
		}
		
		return inside;
	}

	/**
	 * @return the set of vertices just beyond the cutoff distance
	 */
	public Set<V> getOutsideBorder() {
		return border.keySet();
	}
	
	/**
	 * @return the set of vertices just beyond and within the cutoff distance
	 */
	public Set<V> getCompleteBorder() {		
		Set<V> all = getInsideBorder();
		all.addAll(border.keySet());
		return all;
	}

}
