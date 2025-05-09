class Pokemon < ApplicationRecord
  # Name is mandatory and unique
  validates :name, presence: true, uniqueness: true
  # Description is optional but cannot exceed 500 characters
  validates :description, length: { maximum: 500 }
  # Capture information is mandatory
  validates :captured, inclusion: { in: [true, false] }
  # Elemental type is mandatory
  validates :elemental_type, presence: true

  belongs_to :elemental_type
end
