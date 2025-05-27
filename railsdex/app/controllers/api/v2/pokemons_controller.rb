class Api::V2::PokemonsController < ApplicationController
  include Api::V2::PokemonsHelper

  # Disables CSRF (Cross-Site Request Forgery) protection
  skip_before_action :verify_authenticity_token
  # Load the Pokémon data and handle eventual RecordNotFound gracefully
  before_action :set_pokemon, only: [:show, :update, :destroy]

  if Rails.env.development?
    # ActiveStorage, when using the Disk service, requires explicit URL options to generate a full static URL for attachments
    ActiveStorage::Current.url_options = Rails.application.routes.default_url_options
  end

  def index
    @pokemons = Pokemon.includes(:elemental_type).all

    respond_to do |format|
      handle_pokemon_data_request(format, @pokemons)
    end
  end

  def show
    respond_to do |format|
      handle_pokemon_data_request(format, @pokemon)
    end
  end

  def create
    @pokemon = Pokemon.new(pokemon_params)

    if @pokemon.save
      respond_to do |format|
        format.any { render json: PokemonJsonSerializer.new(@pokemon).as_json, status: :created }
        format.xml { render xml: PokemonXmlGeneratorService.call(@pokemon), status: :created }
      end
    else
      respond_to do |format|
        format.any { render json: @pokemon.errors, status: :unprocessable_entity }
        format.xml { render xml: @pokemon.errors.full_messages, status: :unprocessable_entity }
      end
    end
  end

  def update
    if @pokemon.update(pokemon_params)
      respond_to do |format|
        format.any { render json: PokemonJsonSerializer.new(@pokemon).as_json, status: :accepted }
        format.xml { render xml: PokemonXmlGeneratorService.call(@pokemon), status: :accepted }
      end
    else
      respond_to do |format|
        format.any { render json: @pokemon.errors, status: :unprocessable_entity }
        format.xml { render xml: @pokemon.errors.full_messages, status: :unprocessable_entity }
      end
    end
  end

  def destroy
    if @pokemon.destroy
      head :no_content
    else
      respond_to do |format|
        format.any { render json: @pokemon.errors, status: :unprocessable_entity }
        format.xml { render xml: @pokemon.errors.full_messages, status: :unprocessable_entity }
      end
    end
  end

  private

  def pokemon_params
    params.permit(:name, :description, :captured, :elemental_type_id, :picture)
  end

  def set_pokemon
    @pokemon = Pokemon.find(params[:id])
  rescue ActiveRecord::RecordNotFound
    respond_to do |format|
      format.any { render json: { error: "Pokemon ##{params[:id]} not found" }, status: :not_found }
      format.xml { render xml: { error: "Pokemon ##{params[:id]} not found" }, status: :not_found }
    end
  end

  def handle_pokemon_data_request(format, data)
    format.any { render json: PokemonJsonSerializer.new(data).as_json }
    format.xml { render xml: PokemonXmlGeneratorService.call(data) }
    format.csv {
      contents = CSV.generate do |csv|
        csv << ["ID", "Name", "Description", "Captured", "Created", "Updated", "Elemental Type ID", "Elemental Type Name", "Picture URL"]
        data = Array.new(1) { data } if data.is_a?(Pokemon)
        data.each do |pokemon|
          csv << [pokemon.id, pokemon.name, (pokemon.description.present? ? pokemon.description : nil), formatted_captured(pokemon),
                  # Format dates as YYYY-MM-DD HH:MM:SS for maximum compatibility and clarity
                  formatted_date_and_time(pokemon.created_at), formatted_date_and_time(pokemon.updated_at),
                  pokemon.elemental_type_id, pokemon.elemental_type.name, (pokemon.picture.present? ? pokemon.picture.url : nil)]
        end
      end

      send_data contents, type: "text/csv", filename: "pokemons.csv"
    }
  end
end
