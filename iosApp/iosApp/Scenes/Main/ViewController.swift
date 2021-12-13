import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm
import SwiftUI
import Network

class ViewController: UIViewController, DataBackDelegate {

    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var textField: UITextField!
    //@IBOutlet private weak var currentCityView: UIView!
    @IBOutlet private weak var upperPanel: UIView!
    @IBOutlet private weak var cb: UIView!
    
    private let refreshControl = UIRefreshControl()
    let ind = Indicator()
    @AppStorage("hide") var hide = HiddenState.shared.state
    @AppStorage("language") var language = LocalizationService.shared.language
    
    var viewModel: SimpleViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        viewModel = SimpleViewModel(eventsDispatcher: .init())
        viewModel.fetchAllCities()
        tableViewRegister()
        cb.layer.cornerRadius = 10
        cb.layoutIfNeeded()
        monitorInternetConnection()
        if hide == true || !Reachability.isConnectedToNetwork() {
            //currentCityView.isHidden = true
            cb.isHidden = true
//            tableView.translatesAutoresizingMaskIntoConstraints = true
//            tableView.topAnchor.constraint(equalTo: upperPanel.bottomAnchor, constant: 0).isActive = true
            
        }
        textField.placeholder = "src".localized(language)
        textField.addTarget(self, action: #selector(textFieldDidChanged), for: .editingDidEndOnExit)
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        if !hide {
        monitorInternetConnection()
        } else {
            cb.isHidden = true
        }
    }
    
    
    func monitorInternetConnection() {
        let monitor = NWPathMonitor()
        monitor.pathUpdateHandler = { path in
            if path.status == .satisfied {
                if !self.hide {
                    DispatchQueue.main.async {
                        self.cb.isHidden = false
                    }
                }
            } else {
                DispatchQueue.main.async {
                    self.cb.isHidden = true
                }
            }
        }
        
        let queue = DispatchQueue(label: "Network")
        monitor.start(queue: queue)
    }
    
    func signalToHide(_ variant: Bool) {
        textField.placeholder = "src".localized(language)
        if variant {
            cb.isHidden = true
            tableView.reloadData()
        } else {
            cb.isHidden = false
            cb.setNeedsDisplay()

            tableView.reloadData()
        }
    }
    
    private func tableViewRegister() {
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(UINib(nibName: "CityCell", bundle: nil), forCellReuseIdentifier: "CityCell")
        tableView.refreshControl = refreshControl
        refreshControl.addTarget(self, action: #selector(refreshWeather), for: .valueChanged)

    }
    
    @objc private func textFieldDidChanged() {
        if textField.text!.isEmpty {
            self.view.endEditing(true)
        } else {
            onCounterButtonPressed()
        }
    }
    
    @IBAction func goToSettings() {
        let sb = UIStoryboard(name: "ViewController", bundle: nil)
        let controller = sb.instantiateViewController(withIdentifier: "SettingsViewController") as! SettingsViewController
        controller.delegate = self
        controller.modalPresentationStyle = .fullScreen
        self.navigationController?.present(controller, animated: true)
    }
    
    @IBAction func onCounterButtonPressed() {
        guard let text = textField.text, !text.isEmpty else { return }
        if !Reachability.isConnectedToNetwork() {
            showMiracle()
        }
        ind.showIndicator()
        viewModel.checkAndAddNewCity(name: text) { result in
            if result == LoadingState.success {
                self.view.endEditing(true)
                self.textField.text = ""
                self.viewModel.fetchAllCities()
                self.tableView.reloadData()
                self.ind.hideIndicator()
            } else {
                self.textField.text = ""
                self.ind.hideIndicator()
                self.showMiracle()
            }
        }
    }
    
    @IBAction func deleteAllCities() {
        viewModel.realm.deleteAllCities()
        viewModel.fetchAllCities()
        tableView.reloadData()
    }
    
    @objc private func refreshWeather() {
        if !Reachability.isConnectedToNetwork() {
            showMiracle()
            tableView.refreshControl?.endRefreshing()
            return
        }
        ind.showIndicator()
        //currentCityView.setNeedsDisplay()
        cb.setNeedsDisplay()
        if viewModel.cities.count != 0 {
            viewModel.refresh {
                self.viewModel.fetchAllCities()
                self.update()
                DispatchQueue.main.async {
                    self.ind.hideIndicator()
                }
            }
        } else {
            update()
            ind.hideIndicator()
        }
        print("Here")
    }
    
    @objc func showMiracle() {
        let slideVC = OverlayView()
        slideVC.viewModel = viewModel
        slideVC.modalPresentationStyle = .custom
        slideVC.transitioningDelegate = self
        self.present(slideVC, animated: true, completion: nil)
    }
    
}


extension ViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        viewModel.cities.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cities = viewModel.cities as! [RealmCityModel]
        let cell = tableView.dequeueReusableCell(withIdentifier: "CityCell", for: indexPath) as! CityCell
        cell.setCellContent(data: cities[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let sb = UIStoryboard(name: "ViewController", bundle: nil)
        let controller = sb.instantiateViewController(withIdentifier: "DetailViewController") as! DetailViewController
        controller.welcome = viewModel.cities[indexPath.row] as! RealmCityModel
        self.present(controller, animated: true, completion: nil)
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        viewModel.deleteCity(name: (viewModel.cities[indexPath.row] as AnyObject).name)
        viewModel.fetchAllCities()
        tableView.reloadData()
    }
    
}

extension ViewController: UIViewControllerTransitioningDelegate {
    func presentationController(forPresented presented: UIViewController, presenting: UIViewController?, source: UIViewController) -> UIPresentationController? {
        PresentationController(presentedViewController: presented, presenting: presenting)
    }
}


extension ViewController: SimpleViewModelEventsListener {
    func error(message: String) {
        print("error")
    }
    
    func isLoading(isLoading: Bool) {
        if isLoading {
            tableView.refreshControl?.beginRefreshing()
        } else {
            tableView.refreshControl?.endRefreshing()
        }
    }
    
    func update() {
        DispatchQueue.main.async {
            self.tableView.reloadData()
            self.tableView.refreshControl?.endRefreshing()
        }
    }
    
    
}
