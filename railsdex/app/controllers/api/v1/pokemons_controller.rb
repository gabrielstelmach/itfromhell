class Api::V1::PokemonsController < ApplicationController
  def index
    @pokemons = Pokemon.all
    render json: @pokemons
  end

  def show
    @pokemon = Pokemon.find(params[:id])
    render json: @pokemon
  end
end
