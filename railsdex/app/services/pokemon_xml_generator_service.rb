require "builder"

# XML generator service for Pokemon object.
# This can be used to generate XML documents from a collection of Pokemon objects as well as a single Pokemon object.
# The XML document contents will be generated in friendly format using 2spaces for indentation.
#
# @example:
#   PokemonXmlGeneratorService.call(Pokemon.all)
#   PokemonXmlGeneratorService.call(Pokemon.find(1))
#
class PokemonXmlGeneratorService
  POKEMON_ELEMENT = "pokemon".freeze

  def self.call(input)
    new(input).call
  end

  # @param input [Array<Pokemon>, Pokemon] The input data.
  def initialize(input)
    @input_data = input
    @root_element = input.is_a?(Enumerable) ? POKEMON_ELEMENT.pluralize : POKEMON_ELEMENT
    @singular_element = input.is_a?(Enumerable) ? POKEMON_ELEMENT : nil
  end

  def call
    if Rails.env.development?
      # ActiveStorage, when using the Disk service, requires explicit URL options to generate a full static URL for attachments
      ActiveStorage::Current.url_options = Rails.application.routes.default_url_options
    end

    build_xml
  end

  private

  # Build the XML document from the input data.
  #
  # @return [String] The XML document contents in friendly format using 2spaces for indentation.
  def build_xml
    # Friendly output
    xml = Builder::XmlMarkup.new(indent: 2)
    xml.instruct! :xml, version: "1.0", encoding: "UTF-8"

    if @singular_element.present?
      xml.tag!(@root_element) do
        @input_data.each do |pokemon|
          write_item(xml, @singular_element, pokemon)
        end
      end
    else
      write_item(xml, @root_element, @input_data)
    end

    xml.target!
  end

  # Write a single item to the XML document.
  #
  # @param xml [Builder::XmlMarkup] The XML document object.
  # @param element [String] The element name -expects <code>pokemon</code>.
  # @param pokemon [Pokemon] Pokémon's details object.
  def write_item(xml, element, pokemon)
    xml.tag!(element) do
      xml.id pokemon.id
      xml.name pokemon.name
      xml.description pokemon.description.present? ? pokemon.description : nil
      xml.captured pokemon.captured?
      xml.created_at pokemon.created_at.iso8601
      xml.updated_at pokemon.updated_at.iso8601
      xml.elemental_type(pokemon.elemental_type.name, id: pokemon.elemental_type.id)
      xml.picture_url pokemon.picture.present? ? pokemon.picture.url : nil
    end
  end
end
