// Configure your import map in config/importmap.rb. Read more: https://github.com/rails/importmap-rails
import "@hotwired/turbo-rails"
import "controllers"

// Add filename when selected to upload the Pokémon picture
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.file-upload input[type="file"]').forEach(input => {
        input.addEventListener('change', function () {
            this.nextElementSibling.nextElementSibling.textContent = this.files.length > 0 ? this.files[0].name : 'No picture chosen';
        });
    });
});