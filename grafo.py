import networkx as nx
import matplotlib.pyplot as plt

# Create graph
city_graph = nx.Graph()

# Tourist locations (different names)
locations = [
    "Carnival Plaza",
    "Nariño Square",
    "Panamerican Avenue",
    "Carnival Street",
    "Cultural Museum",
    "Hummingbird Monument",
    "Sun Square",
    "Children Park"
]

# Add nodes
city_graph.add_nodes_from(locations)

# New routes (different distances & order)
connections = [
    ("Carnival Plaza", "Nariño Square", 3),
    ("Nariño Square", "Panamerican Avenue", 5),
    ("Panamerican Avenue", "Carnival Street", 2),
    ("Carnival Street", "Cultural Museum", 4),
    ("Cultural Museum", "Hummingbird Monument", 7),
    ("Hummingbird Monument", "Sun Square", 3),
    ("Sun Square", "Children Park", 4),
    ("Children Park", "Carnival Plaza", 6),
    ("Nariño Square", "Carnival Street", 4),
    ("Carnival Plaza", "Children Park", 9)
]

# Add weighted edges
for start, end, w in connections:
    city_graph.add_edge(start, end, weight=w)

print("Welcome to the Carnival Route System (Pasto, Colombia)")
print("\nAvailable locations:")
for i, loc in enumerate(locations, 1):
    print(f"{i}. {loc}")

# User input
start_id = int(input("\nEnter start location number: ")) - 1
end_id = int(input("Enter destination location number: ")) - 1

start_point = locations[start_id]
end_point = locations[end_id]

# Calculate shortest path
try:
    path = nx.dijkstra_path(city_graph, start_point, end_point)
    distance = nx.dijkstra_path_length(city_graph, start_point, end_point)

    print(f"\nShortest route from {start_point} to {end_point}:")
    print(" -> ".join(path))
    print(f"Estimated distance: {distance} units")

except nx.NetworkXNoPath:
    print("No available route between selected locations.")

# Visualize graph
pos = nx.spring_layout(city_graph, seed=10)
weights = nx.get_edge_attributes(city_graph, 'weight')

nx.draw(
    city_graph, pos, with_labels=True,
    node_color="lightgreen", edge_color="black", node_size=2200, font_size=9
)
nx.draw_networkx_edge_labels(city_graph, pos, edge_labels=weights)

plt.title("Main Carnival Routes - Pasto, Colombia")
plt.show()
