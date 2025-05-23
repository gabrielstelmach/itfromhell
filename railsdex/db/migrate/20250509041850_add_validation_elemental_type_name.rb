class AddValidationElementalTypeName < ActiveRecord::Migration[8.0]
  def change
    # Name is mandatory
    change_column_null :elemental_types, :name, false
    # Name is unique
    add_index :elemental_types, :name, unique: true
  end
end
