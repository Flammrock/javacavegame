/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

/**
 *
 * @author User
 */
public class Talisman implements Copiable {
    
    private String name;
    private String originRoomName;
    
    public Talisman(String roomName, String name) {
        this.originRoomName = roomName;
        this.name = name;
    }
    
    public Talisman(String name) {
        this("",name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginRoomName() {
        return originRoomName;
    }

    public void setOriginRoomName(String originRoomName) {
        this.originRoomName = originRoomName;
    }
    
    
    
    @Override
    public Copiable copy() {
        return new Talisman(this.originRoomName,this.name);
    }
    
    
}
