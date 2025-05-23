class CreatePokemons < ActiveRecord::Migration[8.0]
  def change
    create_table :pokemons do |t|
      t.string :name
      t.text :description
      t.boolean :captured

      t.timestamps
    end
  end
end
