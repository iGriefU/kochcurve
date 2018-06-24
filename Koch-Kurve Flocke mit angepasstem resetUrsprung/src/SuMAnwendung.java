
import sum.ereignis.*;
import sum.komponenten.*;

import java.awt.*;

public class SuMAnwendung extends EBAnwendung
{

    // Knöpfe zum Ausführen, Textfelder zur Parametereingabe, Etikette zur Beschreibung
    Knopf kZeichne, kLoesche, evolution;
    Etikett eZeichne, eStufe, eLaenge;
    Textfeld tLaenge, tStufe,tZeit;
    Buntstift derStift;
    Regler regler;
    Schalter checkbox;
    static Color grau = Color.darkGray;
    static Color weiss = Color.white;

    long internalTimeout = 0;


    public SuMAnwendung()
    {
        super(900,800);
        super.hatBildschirm.setTitle("Kochsches Schneeflöckchen");

        //Etikette
        eZeichne = new  Etikett(55,50,100,50,"Zeichne");
        eLaenge  = new Etikett(170,50,100,50,"Bodenl\u00e4nge");
        eStufe  = new  Etikett(290,50,100,50,"Rekursionsstufen");

        //Knöpfe
        kZeichne = new Knopf(50,100,100,25,"Zeichne","zeichne");
        kLoesche = new Knopf(50,125,100,25,"L\u00f6sche","loesche");

        //Textfelder
        tLaenge = new Textfeld(170,100,100,50,"500");
        tStufe  = new Textfeld(290,100,100,50,"0");

        //Regler Slider
        regler = new Regler(400,100,200,50,0,0,8);
        regler.setzeBearbeiterGeaendert("reglerBearbeiter");

        //Schalter Checkbox
        checkbox = new Schalter(650,75 ,100,25,"Zeit einstellen? ","actionSchalter");
        tZeit    = new Textfeld(650,100,100,25,"0");
        tZeit.verstecke();
        tZeit.setzeInhalt(0);

        //Evoluion
        evolution = new Knopf(775,100,100,50,"Evolution","handleEvolution");

        derStift = new Buntstift();
        derStift.setzeFarbe(grau);

        internalTimeout = 0;

    }

    public void zeichne(){  //zeichenknopf actionlistener

        //super.hatBildschirm.setBounds(super.hatBildschirm.getX(), super.hatBildschirm.getY(), 50 + super.hatBildschirm.breite() + 50, 160 + this.hoeheForm() + 50);
        //passt das Fenster an die zu zeichnende Form an -> links, rechts, unter der form ist 50px platz, unter
        // TODO: refresh JFrame und dann zeichnen

        this.setInternalTimeout();
        this.resetUrsprung();

        derStift.runter();
        for(int i=1; i<4; i++) {
            rekursiv(tLaenge.inhaltAlsZahl());
            derStift.dreheUm(-120);
        }
        derStift.hoch();

    }

    public void loesche(){  //loescheknopf actionlistener

        setInternalTimeout();
        derStift.bewegeBis(50, 160);

        /*derStift.radiere();

        derStift.runter();
        for(int i=1;i<4;i++) {
            rekursiv(tLaenge.inhaltAlsZahl());
            derStift.dreheUm(-120);
        }
        derStift.hoch();

        derStift.normal();*/
        derStift.setzeFarbe(weiss);
        derStift.setzeFuellmuster(Muster.GEFUELLT);
        derStift.zeichneRechteck(super.hatBildschirm.breite(),super.hatBildschirm.hoehe());
        derStift.setzeFarbe(grau);
        this.resetUrsprung();

    }

    public void handleEvolution(){
        if(tStufe.inhaltAlsText()==""){
            tStufe.setzeInhalt(0);
            this.handleEvolution();
        }else {

        this.loesche();
        this.zeichne();
        tStufe.setzeInhalt(tStufe.inhaltAlsGanzeZahl()+1);
        }
    }

    public void reglerBearbeiter(){
        tStufe.setzeInhalt( (int) regler.wert() );

    }

    public void actionSchalter(){  //schalterBearbeiter
        if(checkbox.angeschaltet()){
            tZeit.zeige();
        }
        else{
            tZeit.verstecke();
        }
    }

    public void rekursiv(double laenge){ //laenge des fundaments  //zeichnet die ecke


        if (laenge <= (tLaenge.inhaltAlsGanzeZahl() / Math.pow(3,tStufe.inhaltAlsZahl())) ) {     //abbruchbedingung  // bei stufe 0 direkt ausgelöst

            try {

                derStift.bewegeUm(laenge);  //linie wird gezeichnet

                if(tZeit.istSichtbar()){ //wenn nicht sichbar dann auf 0 gesetzt und keine zeitverzögerung

                    Thread.sleep(internalTimeout);   //berechnete zeit wird gewartet

                }
            }
            catch(Exception e) {
                System.out.println(""+ e.toString() +""); //fatales errorhandling nicht nötig**
            }
        }
        else
            {
                this.rekursiv(laenge / 3);     // Eckfraktale werden in der tieferen instanz gezeichnet
                derStift.dreheUm(60);    //diese instanz ist trotzdem eine Ecke
                this.rekursiv(laenge / 3);
                derStift.dreheUm(-180 + 60);
                this.rekursiv(laenge / 3);
                derStift.dreheUm(60);
                this.rekursiv(laenge / 3);

        }
    }

    public void resetUrsprung(){

        derStift.bewegeBis(50, 160 + Math.sqrt(Math.pow((tLaenge.inhaltAlsZahl() / 3), 2)
                - Math.pow((tLaenge.inhaltAlsZahl() / 6), 2)));
    }

    public void setInternalTimeout(){

        if(!tZeit.istSichtbar()){
            tZeit.setzeInhalt(0);
        }
        internalTimeout = (long) (tZeit.inhaltAlsZahl()/(3 * Math.pow(4,tStufe.inhaltAlsGanzeZahl())) );
        //zeit für das zeichnen der flocke
    }

    public void setIT(int x){
        this.internalTimeout = x;
    }

    public int breiteForm(){

    return (
            tLaenge.inhaltAlsGanzeZahl()
    );  //a

    }

    public int hoeheForm(){ // gibts die höhe der schneeflocke vom höchsten punkt bis zum tiefsten punkt

        return (int) (
                Math.sqrt( Math.pow(  tLaenge.inhaltAlsZahl(),   2 )  - Math.pow(tLaenge.inhaltAlsZahl()/2 , 2 ) )    //gegenkathete des dreiecks stufe 0, großes
                +
                Math.sqrt( Math.pow(  tLaenge.inhaltAlsZahl()/3, 2 )  - Math.pow(tLaenge.inhaltAlsZahl()/6 , 2 ) )    //gegenkathte des dreiecks stufe 1, kleines
        );  //b
    }

}
