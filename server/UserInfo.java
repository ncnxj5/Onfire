
public class UserInfo {
	int x;
	int y;
	int lifePoint = 100;
	public UserInfo(){
		this.x = 0;
		this.y = 0;
	}
	public void setPos(int x,int y){
		this.x=x;
		this.y=y;
	}
	public void setLP(int lp){
		this.lifePoint = lp;
	}
}
