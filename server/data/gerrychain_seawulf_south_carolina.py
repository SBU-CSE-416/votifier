from gerrychain import Graph, Partition
from gerrychain.updaters import Tally, cut_edges
import argparse

def compute_plans(plan_name, output):
  # Load the graph in from the provided json file
  graph = Graph.from_json("precinct_dual_graph_SC_output.json")

  # Set up the initial partition object
  initial_partition = Partition(
    graph,
    assignment="placement_2022",
    updaters={
        "population": Tally("TOT_POP", alias="population"),
        "cut_edges": cut_edges,
    }
  )
  
  return

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('--plan_name', type=str, required=True)
    parser.add_argument('--output', type=str, required=True)

    args = parser.parse_args()
    compute_plans(args.plan_name, args.output)