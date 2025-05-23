class ElementalType < ApplicationRecord
  # Name is mandatory and unique
  validates :name, presence: true, uniqueness: true

  has_many :pokemons
end
