import com.shahrukh.mansuri.hr.bl.interfaces.managers.*;
import com.shahrukh.mansuri.hr.bl.interfaces.pojo.*;
import com.shahrukh.mansuri.hr.bl.exceptions.*;
import com.shahrukh.mansuri.hr.bl.pojo.*;
import com.shahrukh.mansuri.hr.bl.managers.*;
import java.util.*;
class DesignationManagerGetDesignationByCodeTestCase
{
public static void main(String gg[])
{
int code=Integer.parseInt(gg[0]);
try
{
DesignationManagerInterface designationManager;
designationManager=DesignationManager.getDesignationManager();
DesignationInterface designation;
designation=designationManager.getDesignationByCode(code);
System.out.printf("Code %d, Title %s\n",designation.getCode(),designation.getTitle());
}catch(BLException blException)
{
List<String> properties=blException.getProperties();
properties.forEach((property)->{
System.out.println(blException.getException(property));
});
}
}
}