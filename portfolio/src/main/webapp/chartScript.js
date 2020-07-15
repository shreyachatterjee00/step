// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

/** Fetches bigfoot sightings data and uses it to create a chart. */
function drawChart() {
  fetch('/titanic-data').then(response => response.json())
  .then((titanicSurvivors) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Class');
    data.addColumn('number', '% Survivors');
    Object.keys(titanicSurvivors).forEach((shipClass) => {
      data.addRow([shipClass, titanicSurvivors[shipClass]]);
    });

    const options = {
      'width':600,
      'height':500
    };

    const chart = new google.visualization.LineChart(
        document.getElementById('chart-container'));
    chart.draw(data, options);
  });

}