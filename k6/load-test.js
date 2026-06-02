import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const errorRate = new Rate('error_rate');
const postTrend = new Trend('post_weight_duration');
const getTrend = new Trend('get_weight_duration');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const TOKEN = __ENV.JWT_TOKEN || 'test-token';

export const options = {
    stages: [
        { duration: '30s', target: 50 },   // opwarmen naar 50 gebruikers
        { duration: '1m',  target: 50 },   // normaal gebruik: 50 gelijktijdige gebruikers
        { duration: '30s', target: 200 },  // piekbelasting: opschalen naar 200
        { duration: '1m',  target: 200 },  // piekbelasting aanhouden
        { duration: '30s', target: 0 },    // afkoelen
    ],
    thresholds: {
        'post_weight_duration{stage:normal}': ['p(95)<300'],  // p95 onder 300ms bij normaal gebruik
        'get_weight_duration{stage:normal}':  ['p(95)<300'],
        'error_rate':                         ['rate<0.01'],  // minder dan 1% fouten
        'http_req_failed':                    ['rate<0.01'],
    },
};

const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${TOKEN}`,
};

export default function () {
    // POST /Weight
    const payload = JSON.stringify({
        name: 'Bench Press',
        category: 'Push',
        weight: 60,
        reps: 8,
    });

    const postRes = http.post(`${BASE_URL}/Weight`, payload, { headers });
    postTrend.add(postRes.timings.duration);

    const postOk = check(postRes, {
        'POST /Weight status is 201': (r) => r.status === 201,
        'POST /Weight no 500 error':  (r) => r.status !== 500,
    });
    errorRate.add(!postOk);

    sleep(0.5);

    // GET /Weight
    const getRes = http.get(`${BASE_URL}/Weight`, { headers });
    getTrend.add(getRes.timings.duration);

    const getOk = check(getRes, {
        'GET /Weight status is 200': (r) => r.status === 200,
        'GET /Weight no 500 error':  (r) => r.status !== 500,
        'GET /Weight returns array': (r) => {
            try {
                const body = JSON.parse(r.body);
                return Array.isArray(body);
            } catch {
                return false;
            }
        },
    });
    errorRate.add(!getOk);

    sleep(0.5);
}