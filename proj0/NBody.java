public class NBody{

    public static double readRadius(String filename){
        In in=new In(filename);     
        int firstItemInFile = in.readInt();
        double secondItemInFile = in.readDouble();
        return secondItemInFile;
		
	}


    public static Planet[] readPlanets(String filename){
        In in=new In(filename);
        int nums=in.readInt();
        double radius=in.readDouble();
        Planet[] planets=new Planet[nums];
        for(int i=0; i<nums; i++){
            double xxPos = in.readDouble();
			double yyPos = in.readDouble();
			double xxVel = in.readDouble();
			double yyVel = in.readDouble();
			double mass = in.readDouble();
            String imgFileName = in.readString();
            planets[i]=new Planet(xxPos,yyPos,xxVel,yyVel,mass,imgFileName);
        }
        return planets;


    }


    public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Please supply a country as a command line argument.");
			System.out.println("For countries with spaces, use an underscore, e.g. South_Korea");
			/* NOTE: Please don't use System.exit() in your code.
			   It will break the autograder. */
		}	
    //Collecting All Needed Input
		 double T = Double.parseDouble(args[0]);
		 double dt = Double.parseDouble(args[1]);
		 String filename = args[2];
		 double radius = readRadius(filename);
		 Planet[] planets = readPlanets(filename);
	// Drawing the Background
		StdDraw.enableDoubleBuffering();
		String background="images/starfield.jpg";
		 StdDraw.setScale(-radius, radius);
		 StdDraw.clear();
		 StdDraw.picture(0, 0, background);
		 StdDraw.show();
	//Drawing Planets
	    for(Planet p : planets){
			p.draw();
		}
		int planetsNums=planets.length;

		double t=0;
		while(t<T){
			double[] xForces=new double[planetsNums];
			double[] yForces=new double[planetsNums];
			for(int i=0;i<planetsNums;i++){
				Planet p=planets[i];
				xForces[i]=p.calcNetForceExertedByX(planets);
				yForces[i]=p.calcNetForceExertedByY(planets);
			}
			for(int i=0;i<planetsNums;i++){
				planets[i].update(dt, xForces[i], yForces[i]);
			}
			StdDraw.picture(0, 0, background);
			for(Planet p : planets){
				p.draw();
			}	
			StdDraw.show();
			StdDraw.pause(10);	
			t+=dt;	


		}




		
	}

}
