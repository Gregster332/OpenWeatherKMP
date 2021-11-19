import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm

class ViewController: UIViewController {

    @IBOutlet private var counterLabel: UILabel!
    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var textField: UITextField!
    
    private var viewModel: SimpleViewModel!
    private var cityArray: [Welcome] = []
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel = SimpleViewModel()
        tableViewRegister()
       
        
        //counterLabel.bindText(liveData: viewModel.counter)
    }
    
    private func tableViewRegister() {
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(UINib(nibName: "CityCell", bundle: nil), forCellReuseIdentifier: "CityCell")

    }
    
    
    
    @IBAction func onCounterButtonPressed() {
        tapBTN(name: textField.text!) { result in
            if result.name != "NONE" {
                
                self.cityArray.append(result)
            }
        }
        tableView.reloadData()
    }
    
    func tapBTN(name: String, completion: @escaping (Welcome) -> ()) {
        viewModel.onCounterButtonPressed(name: name)
        if viewModel.counter.value != nil {
            completion(viewModel.counter.value!)
        }
    }
    
    override func didMove(toParent parent: UIViewController?) {
        if(parent == nil) { viewModel.onCleared() }
    }
}


extension ViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        cityArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CityCell", for: indexPath) as! CityCell
        cell.setCellContent(data: cityArray[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let sb = UIStoryboard(name: "ViewController", bundle: nil)
        let controller = sb.instantiateViewController(withIdentifier: "DetailViewController") as! DetailViewController
        self.navigationController?.pushViewController(controller, animated: true)
    }
    
    
}
