Rails.application.routes.draw do
  resources :pokemons
  get "home/index"

  get "up" => "rails/health#show", as: :rails_health_check

  root "home#index"
end
