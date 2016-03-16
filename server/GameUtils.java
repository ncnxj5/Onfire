import org.json.JSONArray;
import org.json.JSONException;

public class GameUtils {
	final static int WEAPON_RIFLE = 0;
	final static int WEAPON_HANDGUN = 1; 
	public static int damageJudge(int resX,int resY,int destX,int destY,int weaponType,JSONArray rotation){
		//private boolean if_shoot(float x1,float y1,float x2, float y2,float x,float y,float z,float treashold)
		//{
		int damage = 0;
		int range = 0;
		switch(weaponType){
			case 0:
				range = 50;
				damage = 20;
				break;
			case 1:
				range = 15;
				damage = 10;
				break;
		}
		try {
		double x = rotation.getDouble(0);
		double y = rotation.getDouble(1);
		double z = rotation.getDouble(2);
		if(Math.sqrt((((double)resX-(double)destX)*((double)resX-(double)destX)
				+((double)resY-(double)destY)*((double)resY-(double)destY)))>range)
			return 0;

		if(z>0.5||z<-0.5)
			return 0;
		if(((y/x*(double)destX-(double)destY-y/x*(double)resX+(double)resY)/Math.sqrt((y/x)*(y/x)+1)<2)
				&&((((double)destY-(double)resY)*y)>0)&&((((double)destX-(double)resX)*x)>0))
			return damage;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
