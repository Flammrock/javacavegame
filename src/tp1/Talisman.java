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
public class Talisman extends Item implements Copiable {
    
    private String originRoomName;
    
    public Talisman(String name, String roomName) {
        super(name);
        this.originRoomName = roomName;
    }
    
    public Talisman(String name) {
        this("",name);
    }

    public String getOriginRoomName() {
        return originRoomName;
    }

    public void setOriginRoomName(String originRoomName) {
        this.originRoomName = originRoomName;
    }
    
    
    
    @Override
    public Copiable copy() {
        return new Talisman(this.getName(),this.getOriginRoomName());
    }
    
    
}
