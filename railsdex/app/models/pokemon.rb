class Pokemon < ApplicationRecord
  # Name is mandatory and unique
  validates :name, presence: true, uniqueness: true
  # Description is optional but cannot exceed 500 characters
  validates :description, length: { maximum: 500 }
  # Capture information is mandatory
  validates :captured, inclusion: { in: [true, false] }
  # Elemental type is mandatory
  validates :elemental_type, presence: true
  # File must be an image of type either PNG or JPEG not larger than 250 KB
  validates :picture, content_type: ["image/png", "image/jpeg"], size: { less_than: 250.kilobytes }

  has_one_attached :picture
  belongs_to :elemental_type
end
