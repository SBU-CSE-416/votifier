import { MapStoreContextProvider } from './stores/MapStore';
import './stylesheets/App.css';
import Votifier from './components/Votifier';

function App() {
  return (
    <MapStoreContextProvider>
      <div>
        <Votifier></Votifier>
      </div>
    </MapStoreContextProvider>
  );
}

export default App;
