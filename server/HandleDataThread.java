
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.json.JSONObject;

public class HandleDataThread implements Runnable {
	// request from client
	private Socket request;

	// request id
	private int requestID;
	//private String requestUserName;

	public HandleDataThread(Socket request,int id) {
		this.request = request;
		this.requestID = id;
	}

	@Override
	public void run() {
		try {
			JSONObject idJSON = new JSONObject("{'ID':"+this.requestID+"}");
			SocketUtil.writeStr2Stream(idJSON.toString(),this.request.getOutputStream());
			//request.setSoTimeout(20000);
			String binaryString = "";
			while (true) {
				// get info from request when getting a socket request
				String reqStr = "";
				try {
					// if read() get a timeout exception
					reqStr = SocketUtil.readStrFromStream(request.getInputStream());
				} catch (SocketTimeoutException e) {
					// then break while loop, stop the service
					System.out.println(SocketUtil.getNowTime() + " : Time is out, request[" + requestID + "] has been closed.");
					break;
				}

				binaryString = reqStr;
				
				JSONObject reqJsonObject = FileUtil.ParseJSON(binaryString);
				JSONObject data = new JSONObject(reqJsonObject.getString("data"));
				//update position
				SocketServer.users.get(this.requestID).setPos(data.getInt("X"), data.getInt("Y"));
				//update attack
				if(data.getString("EventType").equals("ATK")){
					for(int i=0;i<SocketServer.getClientsNum();i++){
						if(!SocketServer.isClientClosed(i)){
							int damage=GameUtils.damageJudge(
										SocketServer.users.get(i).x,
										SocketServer.users.get(i).y,
										SocketServer.users.get(this.requestID).x,
										SocketServer.users.get(this.requestID).y,
										data.getInt("WeaponType"),
										data.getJSONArray("rotation")
										
							);

							SocketServer.users.get(i).lifePoint-=damage;
							if(damage!=0)
								SocketUtil.writeStr2Stream(damage+"" , SocketServer.getClientByID(i).getOutputStream());
						}
					}
				}	
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (request != null) {
				try {
					request.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
