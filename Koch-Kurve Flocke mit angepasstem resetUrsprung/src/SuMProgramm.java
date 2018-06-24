
public class SuMProgramm
{
    private static SuMAnwendung meineAnwendung;
       
    public static void main(String args[])
    {
        meineAnwendung = new SuMAnwendung();
        meineAnwendung.bildschirm().setAlwaysOnTop(false);
    }
}
