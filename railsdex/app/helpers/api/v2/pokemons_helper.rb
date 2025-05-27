# Helper methods for formatting Pokemon attributes in the API v2.
#
# @example
#   include Api::V2::PokemonsHelper
#   formatted_captured(true) # => "Yes"
#   formatted_date_and_time(Time.current) # => "2025-05-22 21:37:00"
#
module Api::V2::PokemonsHelper
  # Returns a human-readable captured status.
  #
  # @param captured [Boolean] The captured status.
  # @return [String] "Yes" if captured is true, "No" otherwise.
  #
  # @example
  #   formatted_captured(true)  # => "Yes"
  #   formatted_captured(false) # => "No"
  def formatted_captured(captured)
    captured ? "Yes" : "No"
  end

  # Formats a DateTime object as a string following "YYYY-MM-DD HH:MM:SS" format.
  #
  # @param date_and_time [DateTime] The date and time to format.
  # @return [String] The formatted date and time.
  #
  # @example
  #   formatted_date_and_time(DateTime.now) # => "2025-05-22 21:41:00"
  def formatted_date_and_time(date_and_time)
    date_and_time.strftime("%Y-%m-%d %H:%M:%S")
  end
end
