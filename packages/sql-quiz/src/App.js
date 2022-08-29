import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import QuestionBank from './pages/QuestionBank';

function App() {
  return (
    <div>
      <Router>
        <Routes>
          <Route path='/' element={<QuestionBank />}/>
          <Route path='question-bank' element={<QuestionBank />}/>

        </Routes>
      </Router>
    </div>
  );
}

export default App;
