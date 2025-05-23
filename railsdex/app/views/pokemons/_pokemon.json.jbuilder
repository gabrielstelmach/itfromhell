json.extract! pokemon, :id, :name, :description, :captured, :created_at, :updated_at
json.url pokemon_url(pokemon, format: :json)
