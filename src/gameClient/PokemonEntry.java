package gameClient;

import org.jetbrains.annotations.NotNull;

public class PokemonEntry implements  Comparable<PokemonEntry> {
    private CL_Pokemon Pokemon;
    double Value;

    public PokemonEntry(double value, CL_Pokemon pokemon) {
        Pokemon=pokemon;
        Value = value;
    }

    public void setValue(double value) {
        Value = value;
    }


    public double getValue() {
        return Value;
    }

    public CL_Pokemon getPokemon() {
        return Pokemon;
    }

    public void setPokemon(CL_Pokemon pokemon) {
        Pokemon = pokemon;
    }

    @Override
    public int compareTo(@NotNull PokemonEntry other) {
        double thisVal = this.Value;
        double otherVal = other.getValue();
        if(thisVal>otherVal){
            return 1;
        }else if(thisVal==otherVal){
            return 0;
        }
        return -1;
    }
}
