package application;

public class Arr¨ºte {
	public int origine;
	public int destination;
	public int poids;

	public Arr¨ºte(int o, int d, int p) {
		this.origine = o;
		this.destination= d;
		this.poids = p;
	}

	public int getorigine() {
		return this.origine;
	}

	public int getdestination() {
		return this.destination;
	}

	public int getpoids() {
		return this.poids;
	}

	public int getnumerovoisin(int a) {
		if (this.origine == a) {
			return this.destination;
		}
		else {
			return this.origine;
		}
	}

}