class AddElementalTypeToPokemon < ActiveRecord::Migration[8.0]
  def change
    add_reference :pokemons, :elemental_type, foreign_key: true
  end
end
