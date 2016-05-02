/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spaceconquest.ObjetCeleste;

/**
 *
 * @author fr456516
 */
public class PlaneteShadoks extends ObjetCeleste{
     //constructeur
    public PlaneteShadoks() {
        super(GestionnaireImage.getInstance().getImagePlaneteShadoks(),1.2);
    }

    @Override
    public String getType() {
        return "planete Shadoks";
    }
}
