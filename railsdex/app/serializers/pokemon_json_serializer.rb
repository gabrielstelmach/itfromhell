# JSON serializer for Pokemon object.
# This can be used to generate JSON contents from a collection of Pokemon objects as well as a single Pokemon object.
#
# @example:
#   PokemonJsonSerializer.new(Pokemon.all).as_json
#   PokemonJsonSerializer.new(Pokemon.find(1)).as_json
#
class PokemonJsonSerializer
  # @param input [Array<Pokemon>, Pokemon] The input data.
  def initialize(input)
    @input = input
  end

  def as_json
    if Rails.env.development?
      # Active Storage, when using the Disk service, requires explicit URL options to generate a full static URL for attachments
      ActiveStorage::Current.url_options = Rails.application.routes.default_url_options
    end

    if @input.is_a?(Enumerable)
      @input.map { |i| serialize(i) }
    else
      serialize(@input)
    end
  end

  private

  # Build the JSON contents from the input data.
  #
  # @return [Hash] The JSON contents.
  def serialize(pokemon)
    json = {}.tap do |hash|
      hash[:id] = pokemon.id
      hash[:name] = pokemon.name
      hash[:description] = pokemon.description if pokemon.description.present?
      hash[:captured] = pokemon.captured?
      hash[:created_at] = pokemon.created_at.iso8601
      hash[:updated_at] = pokemon.updated_at.iso8601
      hash[:elemental_type] = { id: pokemon.elemental_type.id, name: pokemon.elemental_type.name }
      hash[:picture_url] = pokemon.picture.url if pokemon.picture.present?
    end
  end
end
