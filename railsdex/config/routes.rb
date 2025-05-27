Rails.application.routes.draw do
  namespace :api do
    namespace :v1 do
      get "pokemons", to: "pokemons#index"
      get "pokemons/:id", to: "pokemons#show"
    end
    namespace :v2 do
      resources :pokemons
    end
  end
  resources :pokemons
  get "home/index"

  get "up" => "rails/health#show", as: :rails_health_check

  root "home#index"
end
