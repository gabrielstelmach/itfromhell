# This file should ensure the existence of records required to run the application in every environment (production,
# development, test). The code here should be idempotent so that it can be executed at any point in every environment.
# The data can then be loaded with the bin/rails db:seed command (or created alongside the database with db:setup).
#
# Example:
#
#   ["Action", "Comedy", "Drama", "Horror"].each do |genre_name|
#     MovieGenre.find_or_create_by!(name: genre_name)
#   end
ElementalType.create!(name: "Bug")
ElementalType.create!(name: "Dark")
ElementalType.create!(name: "Dragon")
ElementalType.create!(name: "Electric")
ElementalType.create!(name: "Fairy")
ElementalType.create!(name: "Fighting")
ElementalType.create!(name: "Fire")
ElementalType.create!(name: "Flying")
ElementalType.create!(name: "Ghost")
ElementalType.create!(name: "Grass")
ElementalType.create!(name: "Ground")
ElementalType.create!(name: "Ice")
ElementalType.create!(name: "Normal")
ElementalType.create!(name: "Poison")
ElementalType.create!(name: "Psychic")
ElementalType.create!(name: "Rock")
ElementalType.create!(name: "Steel")
ElementalType.create!(name: "Water")