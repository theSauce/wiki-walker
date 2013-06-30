package org.wiki.walker;

public class WikiNode implements Comparable<WikiNode> {

	private int id;
	private int shortestDistance;
	
	public WikiNode(){}
	
	public WikiNode(int id){ this.id = id; }
	
	@Override
	public int compareTo(WikiNode o) {
		return this.id - o.id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WikiNode other = (WikiNode) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShortestDistance() {
		return shortestDistance;
	}

	public void setShortestDistance(int shortestDistance) {
		this.shortestDistance = shortestDistance;
	}

	@Override
	public String toString(){
		return String.valueOf(id);
	}
}
