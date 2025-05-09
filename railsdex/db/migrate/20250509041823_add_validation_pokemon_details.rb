class AddValidationPokemonDetails < ActiveRecord::Migration[8.0]
  def change
    # Name is mandatory
    change_column_null :pokemons, :name, false
    # Name is unique
    add_index :pokemons, :name, unique: true
    # Capture information is mandatory
    change_column_null :pokemons, :captured, false
    # Elemental Type is mandatory
    change_column_null :pokemons, :elemental_type_id, false
  end
end
