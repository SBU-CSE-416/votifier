import requests

# Define your API key
api_key = 'd80bd509a22b6f1f21706609ad0b09afe39b2759'

# Define the range of years
years = range(2020, 2024)  # Example for years 2020 to 2023

# Loop through each year and make a request
for year in years:
    response = requests.get(
        f'https://api.census.gov/data/timeseries/poverty/saipe?get=NAME,SAEMHI_PT,SAEPOVRTALL_PT&for=county:*&in=state:45&time={year}&key={api_key}'
    )
    if response.status_code == 200:
        data = response.json()
        print(f"Data for year {year}:", data)
    else:
        print(f"Failed to fetch data for year {year}: {response.status_code}")
