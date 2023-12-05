import axios from 'axios';

export default {

    getBreweries() {
        return axios.get('/breweries')
    },

    addBrewery(brewery) {
        return axios.post('/breweries', brewery)
    },

}