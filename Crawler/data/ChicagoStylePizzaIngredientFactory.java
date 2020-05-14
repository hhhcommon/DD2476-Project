38
https://raw.githubusercontent.com/piyush6348/Design-Patterns/master/Factory%20Patterns/Abstract%20Factory%20Pattern/src/IngredientFactory/ChicagoStylePizzaIngredientFactory.java
package IngredientFactory;

import Ingredients.Classes.*;
import Ingredients.Interfaces.ICheese;
import Ingredients.Interfaces.IDough;
import Ingredients.Interfaces.ISauce;
import Ingredients.Interfaces.IVeggie;

public class ChicagoStylePizzaIngredientFactory implements IPizzaIngredientFactory {
    @Override
    public IDough createDough() {
        return new ThinCrustDough();
    }

    @Override
    public ISauce createSauce() {
        return new TomatoSauce();
    }

    @Override
    public ICheese createCheese() {
        return new MozarrellaCheese();
    }

    @Override
    public IVeggie[] createVeggies() {
        return new IVeggie[] {new Veggie1(), new Veggie2()};
    }
}
