import './App.css';
import './assets/styleSheet/tailwindcss.css';
import './assets/styleSheet/common.css'
import {Route, Routes} from "react-router-dom";
import Home from "./pages/Home.jsx";

function App() {

    return (
        <div className="App">
            <Routes>
                <Route path="/" element={<Home/>} />

            </Routes>
        </div>
    );
}

export default App
