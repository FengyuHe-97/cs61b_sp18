public class Planet {
	public double xxPos;	//Its current x position
	public double yyPos;	//Its current y position
	public double xxVel;	//Its current velocity in the x direction
	public double yyVel;	//Its current velocity in the y direction
	public double mass;	//Its mass
	public String imgFileName;	//The name of the file that corresponds to the image that depicts the planet
	private static final double G = 6.67e-11;	//The gravitational constant  

	
    public Planet(double xP, double yP, double xV, double yV, double m, String img){
		xxPos=xP;
		yyPos=yP;
		xxVel=xV;
		yyVel=yV;
		mass=m;
		imgFileName=img;
	}
	
	public Planet(Planet p){
		xxPos=p.xxPos;
		yyPos=p.yyPos;
		xxVel=p.xxVel;
		yyVel=p.yyVel;
		mass=p.mass;
		imgFileName=p.imgFileName;
	}
	
	public double calcDistance(Planet p){
		double res=0;
		res += Math.pow(p.xxPos - xxPos, 2);
		res += Math.pow(p.yyPos - yyPos, 2);
		res = Math.pow(res, 0.5);
		return res;
	}
	
	public double calcForceExertedBy(Planet p){
		double res=0;
		res = G * mass * p.mass;
		res /= Math.pow(calcDistance(p), 2);
		return res;
		
	}
	
	public double calcForceExertedByX(Planet p){
		double res=0;
		res = calcForceExertedBy(p)*(p.xxPos-xxPos)/calcDistance(p);
		return res;
	}

	public double calcForceExertedByY(Planet p){
	    double res=0;
	    res=calcForceExertedBy(p)*(p.yyPos-yyPos)/calcDistance(p);
	    return res;	
	}
	
	private boolean equal(Planet p){
		boolean res=true;
		if(xxPos !=p.xxPos ||yyPos!=p.yyPos){
			res=false;
		}
		return res;
	}
	
    public double calcNetForceExertedByX(Planet[] allPlanets){
		double res=0;
		for(Planet p: allPlanets){
			if(!equal(p)){
				res+=calcForceExertedByX(p);
			}
		}
		return res;
	}
		
    public double calcNetForceExertedByY(Planet[] allPlanets){
		double res=0;
		for(Planet p: allPlanets){
			if(!equal(p)){
				res+=calcForceExertedByY(p);
			}
		}
		return res;
	}	

	public void update(double dt, double xxForce, double yyForce){
		double xxAcc,yyAcc;
		xxAcc=xxForce/mass;
		yyAcc=yyForce/mass;
		xxVel+=dt*xxAcc;
		yyVel+=dt*yyAcc;
		xxPos+=dt*xxVel;
		yyPos+=dt*yyVel;
	}
	
	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}

	
}
